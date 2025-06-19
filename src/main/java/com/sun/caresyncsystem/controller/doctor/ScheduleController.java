package com.sun.caresyncsystem.controller.doctor;

import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.service.ScheduleService;
import com.sun.caresyncsystem.utils.AppConstants;
import com.sun.caresyncsystem.utils.JwtUtil;
import com.sun.caresyncsystem.utils.MessageUtil;
import com.sun.caresyncsystem.utils.api.DoctorApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController(DoctorApiPaths.Schedule.DOCTOR_SCHEDULE_CONTROLLER)
@RequiredArgsConstructor
@RequestMapping(DoctorApiPaths.Schedule.BASE)
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final MessageUtil messageUtil;

    @PostMapping(DoctorApiPaths.Schedule.IMPORT)
    public ResponseEntity<BaseApiResponse<Void>> importSchedules(
            @RequestParam(AppConstants.IMPORT_TYPE) MultipartFile file,
            @AuthenticationPrincipal Jwt jwt
    ) throws IOException {
        Long doctorId = JwtUtil.extractUserIdFromJwt(jwt);
        scheduleService.importSchedules(file.getInputStream(), doctorId);

        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                messageUtil.getMessage("schedule.import.success")
        ));
    }
}
