package com.sun.caresyncsystem.service;

public interface PasswordService {
    String encodePassword(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
