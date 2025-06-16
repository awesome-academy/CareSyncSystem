package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.dto.request.DoctorSearchRequest;
import com.sun.caresyncsystem.dto.response.DoctorSearchResponse;
import com.sun.caresyncsystem.repository.DoctorRepository;
import com.sun.caresyncsystem.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public Page<DoctorSearchResponse> searchDoctors(DoctorSearchRequest request, Pageable pageable) {
        return doctorRepository.searchDoctors(
                request.name(),
                request.specialty(),
                request.service(),
                request.location(),
                request.weekday(),
                request.startTime(),
                request.endTime(),
                pageable
        );
    }
}
