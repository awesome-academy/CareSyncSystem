package com.sun.caresyncsystem.service;

import com.nimbusds.jose.JOSEException;
import com.sun.caresyncsystem.dto.request.LoginRequest;
import com.sun.caresyncsystem.dto.request.VerifyTokenRequest;
import com.sun.caresyncsystem.dto.response.LoginResponse;
import com.sun.caresyncsystem.dto.response.VerifyTokenResponse;

import java.text.ParseException;

public interface AuthenticationService {
     void activateAccount(String token);
     LoginResponse login(LoginRequest request);
     VerifyTokenResponse verifyToken(VerifyTokenRequest request) throws JOSEException, ParseException;
}
