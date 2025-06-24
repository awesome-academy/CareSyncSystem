package com.sun.caresyncsystem.controller;

import com.sun.caresyncsystem.dto.request.CreateBookingRequest;
import com.sun.caresyncsystem.dto.request.RescheduleBookingRequest;
import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.service.BookingService;
import com.sun.caresyncsystem.utils.JwtUtil;
import com.sun.caresyncsystem.utils.MessageUtil;
import com.sun.caresyncsystem.utils.api.BookingApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BookingApiPaths.BASE)
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final MessageUtil messageUtil;

    @PostMapping
    public ResponseEntity<BaseApiResponse<Void>> createBooking(
            @RequestBody @Valid CreateBookingRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = JwtUtil.extractUserIdFromJwt(jwt);
        bookingService.createBooking(userId, request);
        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                messageUtil.getMessage("booking.create.success")
        ));
    }

    @PutMapping(BookingApiPaths.Endpoint.RESCHEDULE)
    public ResponseEntity<BaseApiResponse<String>> rescheduleBooking(
            @PathVariable Long id,
            @Valid @RequestBody RescheduleBookingRequest request
    ) {
        bookingService.rescheduleBooking(id, request);

        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                messageUtil.getMessage("booking.reschedule.success")
        ));
    }

    @PutMapping(BookingApiPaths.Endpoint.CANCEL)
    public ResponseEntity<BaseApiResponse<String>> cancelBooking(
            @PathVariable Long id
    ) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                messageUtil.getMessage("booking.update.success")
        ));
    }
}
