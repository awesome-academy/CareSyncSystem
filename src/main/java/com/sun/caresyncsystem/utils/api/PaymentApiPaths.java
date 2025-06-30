package com.sun.caresyncsystem.utils.api;

public class PaymentApiPaths {
    public static final String PREFIX = "/api/v1";
    public static final String BASE = PREFIX + "/payment";

    public static class Endpoint {
        public static final String CREATE_VNPAY = "/vnpay/create";
        public static final String VNPAY_RETURN = "/vnpay/return";
        public static final String VNPAY_REFUND = "/vnpay/refund";
        public static final String FULL_CREATE_VNPAY = BASE +  CREATE_VNPAY;
    }
}
