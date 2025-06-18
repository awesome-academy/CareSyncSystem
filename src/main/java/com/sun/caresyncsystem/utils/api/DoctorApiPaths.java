package com.sun.caresyncsystem.utils.api;

public class DoctorApiPaths {
    public static final String BASE = "/api/v1/doctors";

    public static class Endpoint {
        public static final String SEARCH = "/search";
        public static final String FULL_SEARCH = BASE + SEARCH;
        public static final String BOOKING_BY_ID = "/bookings/{id}";
    }
}
