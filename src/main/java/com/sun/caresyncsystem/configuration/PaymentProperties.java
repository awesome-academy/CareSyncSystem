package com.sun.caresyncsystem.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:payment-info.properties")
@ConfigurationProperties(prefix = "payment")
@Data
public class PaymentProperties {
    private VNPay vnPay;

    @Data
    public static class VNPay {
        private String tmnCode;
        private String hashSecret;
        private String payUrl;
        private String refundUrl;
    }
}
