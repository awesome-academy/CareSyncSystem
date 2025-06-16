package com.sun.caresyncsystem.controller;

import com.nimbusds.jose.JOSEException;
import com.sun.caresyncsystem.dto.request.LoginRequest;
import com.sun.caresyncsystem.dto.request.LogoutRequest;
import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.dto.response.LoginResponse;
import com.sun.caresyncsystem.service.AuthenticationService;
import com.sun.caresyncsystem.utils.MessageUtil;
import com.sun.caresyncsystem.utils.api.AuthApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping(AuthApiPaths.BASE)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final MessageUtil messageUtil;

    @GetMapping(AuthApiPaths.Endpoint.ACTIVATE)
    public ResponseEntity<BaseApiResponse<String>> activateAccount(@RequestParam String token) {
        authenticationService.activateAccount(token);

        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                messageUtil.getMessage("auth.activate.account.success")
        ));
    }

    @PostMapping(AuthApiPaths.Endpoint.LOGIN)
    public ResponseEntity<BaseApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                authenticationService.login(loginRequest)
        ));
    }

    @PostMapping(AuthApiPaths.Endpoint.LOGOUT)
    public ResponseEntity<BaseApiResponse<Void>> logout(@RequestBody LogoutRequest request)
            throws JOSEException, ParseException {
        authenticationService.logout(request);

        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseApiResponse<>(
                        HttpStatus.OK.value(),
                        messageUtil.getMessage("auth.logout.success")
                )
        );
    }
}
