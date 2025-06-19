package com.sun.caresyncsystem.utils.api;

public class DoctorApiPaths {
    public static final String BASE = "/api/v1/doctors";
    public static final String BASE_ALL = BASE + "/**";

    public static class Endpoint {
        public static final String SEARCH = "/search";
        public static final String FULL_SEARCH = BASE + SEARCH;
    }

    public static final class Booking {
        public static final String DOCTOR_BOOKING_CONTROLLER = "doctorBookingController";
        public static final String BASE = DoctorApiPaths.BASE + "/bookings";
        public static final String BOOKING_BY_ID = "/{id}";
    }

    public static final class Schedule {
        public static final String DOCTOR_SCHEDULE_CONTROLLER = "doctorScheduleController";
        public static final String BASE = DoctorApiPaths.BASE + "/schedules";
        public static final String IMPORT = "/import";
    }
}
