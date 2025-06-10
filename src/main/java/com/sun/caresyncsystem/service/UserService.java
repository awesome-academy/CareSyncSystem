package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.CreateUserRequest;
import com.sun.caresyncsystem.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
}
