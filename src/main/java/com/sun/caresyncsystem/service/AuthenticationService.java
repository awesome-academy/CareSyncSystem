package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.LoginRequest;
import com.sun.caresyncsystem.dto.response.LoginResponse;

public interface AuthenticationService {
     void activateAccount(String token);
     LoginResponse login(LoginRequest request);
}
