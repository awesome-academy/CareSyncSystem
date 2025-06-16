package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.DoctorSearchRequest;
import com.sun.caresyncsystem.dto.response.DoctorSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    Page<DoctorSearchResponse> searchDoctors(DoctorSearchRequest request, Pageable pageable);
}
