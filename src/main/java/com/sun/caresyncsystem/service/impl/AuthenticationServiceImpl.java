package com.sun.caresyncsystem.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sun.caresyncsystem.configuration.SecurityProperties;
import com.sun.caresyncsystem.dto.request.LoginRequest;
import com.sun.caresyncsystem.dto.request.VerifyTokenRequest;
import com.sun.caresyncsystem.dto.response.LoginResponse;
import com.sun.caresyncsystem.dto.response.VerifyTokenResponse;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.model.entity.VerificationToken;
import com.sun.caresyncsystem.repository.InvalidatedTokenRepository;
import com.sun.caresyncsystem.repository.UserRepository;
import com.sun.caresyncsystem.repository.VerificationTokenRepository;
import com.sun.caresyncsystem.service.AuthenticationService;
import com.sun.caresyncsystem.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final String CLAIM_SCOPE = "scope";
    private static final String CLAIM_USER_ID = "userId";

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordService passwordService;
    private final SecurityProperties securityProperties;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Transactional
    public void activateAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_VERIFICATION_TOKEN));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_VERIFICATION_TOKEN);
        }

        User user = verificationToken.getUser();
        user.setActive(true);

        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findUserByEmail(request.email())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        boolean isSuccess = passwordService.matches(request.password(), user.getPassword());
        if (!isSuccess)
            throw new AppException(ErrorCode.LOGIN_FAILED);
        String token = generateToken(user);

        return new LoginResponse(true, token);
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(securityProperties.getJwt().getDomain())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim(CLAIM_SCOPE, user.getRole())
                .claim(CLAIM_USER_ID, user.getId())
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(securityProperties.getJwt().getSignerKey().getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(ErrorCode.CAN_NOT_CREATE_TOKEN);
        }
    }

    public VerifyTokenResponse verifyToken(VerifyTokenRequest request)
            throws JOSEException, ParseException {
        boolean isValid = true;
        try {
            checkValidToken(request.token());
        } catch (AppException e) {
            isValid = false;
        }

        return new VerifyTokenResponse(isValid);
    }

    private void checkValidToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(securityProperties.getJwt().getSignerKey().getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiration.after(new Date())))
            throw new AppException(ErrorCode.INVALID_TOKEN);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.INVALID_TOKEN);

    }
}
