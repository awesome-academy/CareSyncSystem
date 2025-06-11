package com.sun.caresyncsystem.controller;

import com.sun.caresyncsystem.dto.request.CreateUserRequest;
import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.dto.response.UserResponse;
import com.sun.caresyncsystem.service.UserService;
import com.sun.caresyncsystem.utils.api.UserApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserApiPaths.BASE)
public class UserController {

    private final UserService userService;

    @PostMapping(UserApiPaths.Endpoint.REGISTER)
    public ResponseEntity<BaseApiResponse<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                userService.createUser(request)
        ));
    }
}
