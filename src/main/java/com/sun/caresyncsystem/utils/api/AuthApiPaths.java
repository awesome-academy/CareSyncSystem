package com.sun.caresyncsystem.utils.api;

public class AuthApiPaths {
    public static final String BASE = "/api/v1/auth";

    public static class Endpoint {
        public static final String LOGIN = "/login";
        public static final String VERIFY_TOKEN = "/verify-token";
        public static final String LOGOUT = "/logout";
        public static final String ACTIVATE = "/activate";
        public static final String FORGOT_PASSWORD = "/forgot-password";
        public static final String RESET_PASSWORD = "/reset-password";

        // Use in security config.
        public static final String FULL_LOGIN = BASE + LOGIN;
        public static final String FULL_VERIFY_TOKEN = BASE + VERIFY_TOKEN;
        public static final String FULL_LOGOUT = BASE + LOGOUT;
        public static final String FULL_ACTIVATE = BASE + ACTIVATE;
        public static final String FULL_FORGOT_PASSWORD = BASE + FORGOT_PASSWORD;
        public static final String FULL_RESET_PASSWORD = BASE + RESET_PASSWORD;
    }
}
