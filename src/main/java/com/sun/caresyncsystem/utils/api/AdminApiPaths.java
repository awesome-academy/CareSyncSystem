package com.sun.caresyncsystem.utils.api;

public class AdminApiPaths {
    public static final String PREFIX = "/v1";
    public static final String BASE = PREFIX + "/admin";
    public static final String BASE_ALL = BASE + "/**";

    public static final class User {
        public static final String ADMIN_USER_CONTROLLER = "adminUserController";
        public static final String BASE = AdminApiPaths.BASE + "/users";
        public static final String BY_ID = "/{id}";
        public static final String APPROVE_DOCTOR =  BY_ID + "/approve-doctor";
        public static final String PENDING_DOCTORS = "/pending-doctors";
        public static final String USER_ACTIVE_STATUS = "/{userId}/active-status";
    }

    private AdminApiPaths() {
        // Prevent instantiation
    }
}
