package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.model.entity.Doctor;
import com.sun.caresyncsystem.model.entity.Schedule;
import com.sun.caresyncsystem.repository.DoctorRepository;
import com.sun.caresyncsystem.repository.ScheduleRepository;
import com.sun.caresyncsystem.service.ScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public void importSchedules(InputStream inputStream, Long doctorId) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getPhysicalNumberOfRows() <= 1) {
                throw new AppException(ErrorCode.IMPORT_FILE_EMPTY);
            }

            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

            Map<String, Integer> headers = mapHeaders(sheet.getRow(0));
            validateRequiredHeaders(headers);

            List<Schedule> schedules = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    Schedule schedule = parseSchedule(row, headers, doctor);
                    schedules.add(schedule);
                } catch (IllegalArgumentException e) {
                    String errorMsg = "Row " + (i + 1) + ": " + e.getMessage();
                    errors.add(errorMsg);
                }
            }

            if (!errors.isEmpty()) {
                throw new AppException(ErrorCode.SCHEDULE_IMPORT_FAIL,
                        "Import failed with the following errors:\n" + String.join("\n", errors));
            }

            scheduleRepository.saveAll(schedules);
            log.info("Imported {} schedules for doctor {}", schedules.size(), doctorId);

        } catch (IOException e) {
            throw new AppException(ErrorCode.SCHEDULE_IMPORT_FAIL);
        }
    }

    private Schedule parseSchedule(Row row, Map<String, Integer> headers, Doctor doctor) {
        try {
            String dateStr = getCellValue(row, headers.get("date"));
            String startStr = getCellValue(row, headers.get("start_time"));
            String endStr = getCellValue(row, headers.get("end_time"));
            String priceStr = getCellValue(row, headers.get("price"));

            if (dateStr.isBlank() || startStr.isBlank() || endStr.isBlank() || priceStr.isBlank()) {
                throw new IllegalArgumentException("Some required fields are empty");
            }

            LocalDate date = LocalDate.parse(dateStr);
            LocalTime start = LocalTime.parse(startStr);
            LocalTime end = LocalTime.parse(endStr);
            BigDecimal price = new BigDecimal(priceStr);

            if (end.isBefore(start)) {
                throw new IllegalArgumentException("End time must be after start time");
            }

            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be a positive number");
            }

            return Schedule.builder()
                    .doctor(doctor)
                    .date(date)
                    .startTime(start)
                    .endTime(end)
                    .price(price)
                    .isAvailable(true)
                    .build();

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Map<String, Integer> mapHeaders(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        for (Cell cell : headerRow) {
            String key = cell.getStringCellValue().trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z_]", "");
            map.put(key, cell.getColumnIndex());
        }
        return map;
    }

    private void validateRequiredHeaders(Map<String, Integer> headers) {
        List<String> required = List.of("date", "start_time", "end_time", "price");
        List<String> missing = required.stream()
                .filter(key -> !headers.containsKey(key))
                .toList();
        if (!missing.isEmpty()) {
            throw new AppException(ErrorCode.SCHEDULE_IMPORT_FAIL,
                    "Missing required columns: " + String.join(", ", missing));
        }
    }

    private String getCellValue(Row row, int column) {
        if (column < 0) throw new IllegalArgumentException("Invalid column index: " + column);
        Cell cell = row.getCell(column);
        if (cell == null) throw new IllegalArgumentException("Missing cell at column " + column);

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            default -> throw new IllegalArgumentException("Unsupported cell type at column " + column);
        };
    }
}
