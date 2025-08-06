package com.sun.caresyncsystem.utils.api;

public class BookingApiPaths {
    public static final String BASE = "/api/v1/bookings";

    public static class Endpoint {
        public static final String RESCHEDULE = "/{id}/reschedule";
        public static final String CANCEL = "/{id}/cancel";
    }
}
