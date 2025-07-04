package com.sun.caresyncsystem.utils;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import com.sun.caresyncsystem.configuration.AppProperties;
import com.sun.caresyncsystem.configuration.PaymentProperties;
import com.sun.caresyncsystem.dto.request.VNPayRefundRequest;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.utils.api.PaymentApiPaths;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RequiredArgsConstructor
public class VNPayUtil {

    public static final String VNP_TRANSACTION_ID_KEY = "vnp_TxnRef";
    public static final String VNP_RESPONSE_CODE_KEY = "vnp_ResponseCode";
    public static final String VNP_SECURE_HASH_KEY = "vnp_SecureHash";
    public static final String VNP_TRANSACTION_NO_KEY = "vnp_TransactionNo";
    public static final String VNP_BANK_CODE_KEY = "vnp_BankCode";
    public static final String VNP_CARD_TYPE_KEY = "vnp_CardType";
    public static final String VNP_PAY_DATE_KEY = "vnp_PayDate";
    private static final String VNP_VERSION = "2.1.0";
    private static final String VNP_COMMAND = "pay";
    private static final String VNP_ORDER_TYPE = "other";
    private static final String VNP_LOCALE = "vn";
    private static final String VNP_CURR_CODE = "VND";
    private static final String VNP_BANK_CODE = "NCB";
    private static final int TIMEZONE_OFFSET_MINUTES = 15;
    private static final char AMPERSAND_CHAR = '&';
    private static final char EQUALS_CHAR = '=';
    public static final String PAYMENT_VNPAY_CODE_SUCCESS = "00";
    public static final String REFUND_VNPAY_CODE_SUCCESS = "02";

    private final PaymentProperties paymentProperties;
    private final AppProperties appProperties;

    public String generatePaymentUrl(String transactionId, BigDecimal amount, HttpServletRequest request) {
        String clientIp = getClientIpAddress(request);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String createDate = formatter.format(calendar.getTime());

        calendar.add(Calendar.MINUTE, TIMEZONE_OFFSET_MINUTES);
        String expireDate = formatter.format(calendar.getTime());

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", VNP_VERSION);
        params.put("vnp_Command", VNP_COMMAND);
        params.put("vnp_TmnCode", paymentProperties.getVnPay().getTmnCode());
        params.put("vnp_Amount", amount.multiply(BigDecimal.valueOf(100)).toBigInteger().toString());
        params.put(VNP_BANK_CODE_KEY, VNP_BANK_CODE);
        params.put("vnp_CurrCode", VNP_CURR_CODE);
        params.put(VNP_TRANSACTION_ID_KEY, transactionId);
        params.put("vnp_OrderInfo", "Payment with booking:" + transactionId);
        params.put("vnp_OrderType", VNP_ORDER_TYPE);
        params.put("vnp_Locale", VNP_LOCALE);
        params.put("vnp_ReturnUrl", appProperties.getBaseUrl() +
                PaymentApiPaths.BASE +
                PaymentApiPaths.Endpoint.VNPAY_RETURN);
        params.put("vnp_IpAddr", clientIp);
        params.put("vnp_CreateDate", createDate);
        params.put("vnp_ExpireDate", expireDate);

        List<String> sortedKeys = new ArrayList<>(params.keySet());
        Collections.sort(sortedKeys);

        StringBuilder queryBuilder = new StringBuilder();

        for (int i = 0; i < sortedKeys.size(); i++) {
            String key = sortedKeys.get(i);
            String value = params.get(key);

            queryBuilder.append(URLEncoder.encode(key, StandardCharsets.US_ASCII));
            queryBuilder.append(EQUALS_CHAR);
            queryBuilder.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

            if (i < sortedKeys.size() - 1) {
                queryBuilder.append(AMPERSAND_CHAR);
            }
        }

        String hashData = queryBuilder.toString();
        String secureHash = hmacSHA512(paymentProperties.getVnPay().getHashSecret(), hashData);

        queryBuilder.append(AMPERSAND_CHAR).append(VNP_SECURE_HASH_KEY).append(EQUALS_CHAR).append(secureHash);

        return UriComponentsBuilder
                .fromUriString(paymentProperties.getVnPay().getPayUrl())
                .query(queryBuilder.toString())
                .build()
                .toUriString();
    }

    public JsonObject refund(VNPayRefundRequest request) {
        String requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        String createDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                .format(Instant.now());

        String vnp_Amount = request.amount().multiply(BigDecimal.valueOf(100)).toBigInteger().toString();

        String hashData = String.join("|",
                requestId,
                "2.1.0",
                "refund",
                paymentProperties.getVnPay().getTmnCode(),
                request.transactionType(),
                request.transactionId(),
                vnp_Amount,
                String.valueOf(request.transactionNo()),
                request.transactionDate(),
                request.createBy(),
                createDate,
                request.ipAddress(),
                request.orderInfo()
        );

        String secureHash = hmacSHA512(paymentProperties.getVnPay().getHashSecret(), hashData);

        JsonObject payload = new JsonObject();
        payload.addProperty("vnp_RequestId", requestId);
        payload.addProperty("vnp_Version", "2.1.0");
        payload.addProperty("vnp_Command", "refund");
        payload.addProperty("vnp_TmnCode", paymentProperties.getVnPay().getTmnCode());
        payload.addProperty("vnp_TransactionType", request.transactionType());
        payload.addProperty("vnp_TxnRef", request.transactionId());
        payload.addProperty("vnp_Amount", vnp_Amount);
        payload.addProperty("vnp_TransactionDate", request.transactionDate());
        payload.addProperty("vnp_CreateBy", request.createBy());
        payload.addProperty("vnp_CreateDate", createDate);
        payload.addProperty("vnp_IpAddr", request.ipAddress());
        payload.addProperty("vnp_OrderInfo", request.orderInfo());
        payload.addProperty(VNP_TRANSACTION_NO_KEY, request.transactionNo());
        payload.addProperty("vnp_SecureHash", secureHash);

        try {
            HttpURLConnection connection = (HttpURLConnection)
                    new URL(paymentProperties.getVnPay().getRefundUrl()).openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder resp = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    resp.append(line);
                }
                return JsonParser.parseString(resp.toString()).getAsJsonObject();
            }

        } catch (IOException e) {
            throw new AppException(ErrorCode.REFUND_FAILED);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        return (ip != null && !ip.isBlank()) ? ip : request.getRemoteAddr();
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] hashBytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            throw new AppException(ErrorCode.GENERATE_PAYMENT_URL_ERROR);
        }
    }

    public boolean verifyReturnDataSignature(Map<String, String> vnpParams) {
        String vnpSecureHash = vnpParams.get(VNP_SECURE_HASH_KEY);

        Map<String, String> filteredParams = new HashMap<>(vnpParams);
        filteredParams.remove(VNP_SECURE_HASH_KEY);

        List<String> sortedKeys = new ArrayList<>(filteredParams.keySet());
        Collections.sort(sortedKeys);

        StringBuilder hashDataBuilder = new StringBuilder();
        for (int i = 0; i < sortedKeys.size(); i++) {
            String key = sortedKeys.get(i);
            String value = filteredParams.get(key);
            hashDataBuilder.append(URLEncoder.encode(key, StandardCharsets.US_ASCII));
            hashDataBuilder.append(EQUALS_CHAR);
            hashDataBuilder.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            if (i < sortedKeys.size() - 1) {
                hashDataBuilder.append(AMPERSAND_CHAR);
            }
        }

        String hashData = hashDataBuilder.toString();
        String calculatedHash = hmacSHA512(paymentProperties.getVnPay().getHashSecret(), hashData);

        return calculatedHash.equalsIgnoreCase(vnpSecureHash);
    }
}
