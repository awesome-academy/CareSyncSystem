package com.sun.caresyncsystem.controller.doctor;

import com.sun.caresyncsystem.dto.request.DoctorSearchRequest;
import com.sun.caresyncsystem.dto.response.DoctorSearchResponse;
import com.sun.caresyncsystem.service.DoctorService;
import com.sun.caresyncsystem.utils.api.DoctorApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DoctorApiPaths.BASE)
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping(DoctorApiPaths.Endpoint.SEARCH)
    public ResponseEntity<Page<DoctorSearchResponse>> searchDoctors(
            @RequestBody DoctorSearchRequest request,
            Pageable pageable
    ) {
        return ResponseEntity.ok(doctorService.searchDoctors(request, pageable));
    }
}
