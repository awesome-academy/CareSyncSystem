package com.sun.caresyncsystem.controller.doctor;

import com.sun.caresyncsystem.dto.request.UpdateBookingStatusRequest;
import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.service.DoctorService;
import com.sun.caresyncsystem.utils.JwtUtil;
import com.sun.caresyncsystem.utils.MessageUtil;
import com.sun.caresyncsystem.utils.api.DoctorApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController(DoctorApiPaths.Booking.DOCTOR_BOOKING_CONTROLLER)
@RequiredArgsConstructor
@RequestMapping(DoctorApiPaths.Booking.BASE)
public class BookingController {
    private final DoctorService doctorService;
    private final MessageUtil messageUtil;

    @PatchMapping(DoctorApiPaths.Booking.BOOKING_BY_ID)
    public ResponseEntity<BaseApiResponse<String>> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookingStatusRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long doctorId = JwtUtil.extractUserIdFromJwt(jwt);
        doctorService.updateBookingStatus(doctorId, id, request.status());

        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                messageUtil.getMessage("booking.update.success")
        ));
    }
}
