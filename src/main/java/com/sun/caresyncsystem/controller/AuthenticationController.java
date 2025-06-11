package com.sun.caresyncsystem.controller;

import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.service.AuthenticationService;
import com.sun.caresyncsystem.utils.MessageUtil;
import com.sun.caresyncsystem.utils.api.AuthApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
