package com.sun.caresyncsystem.controller.admin;

import com.sun.caresyncsystem.dto.request.ReviewDoctorRegistrationRequest;
import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.dto.response.UserResponse;
import com.sun.caresyncsystem.service.UserService;
import com.sun.caresyncsystem.utils.MessageUtil;
import com.sun.caresyncsystem.utils.api.AdminApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(AdminApiPaths.User.ADMIN_USER_CONTROLLER)
@RequiredArgsConstructor
@RequestMapping(AdminApiPaths.User.BASE)
public class UserController {

    private final UserService userService;
    private final MessageUtil messageUtil;

    @GetMapping(AdminApiPaths.User.PENDING_DOCTORS)
    public ResponseEntity<Page<UserResponse>> getPendingDoctors(Pageable pageable) {
        return ResponseEntity.ok(userService.getPendingDoctors(pageable));
    }

    @GetMapping(AdminApiPaths.User.BY_ID)
    public ResponseEntity<BaseApiResponse<UserResponse>> getDoctorByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                userService.getUserByUserId(id)
        ));
    }

    @PutMapping(AdminApiPaths.User.APPROVE_DOCTOR)
    public ResponseEntity<BaseApiResponse<String>> reviewDoctorRegistration(
            @PathVariable Long id,
            @RequestBody @Valid ReviewDoctorRegistrationRequest request) {
        userService.reviewDoctorRegistration(id, request);
        String messageKey = request.isApproved()
                ? "auth.doctor.approved.success"
                : "auth.doctor.rejected.success";

        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                messageUtil.getMessage(messageKey)
        ));
    }
}
