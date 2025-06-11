package com.sun.caresyncsystem.configuration;

import com.sun.caresyncsystem.model.enums.UserRole;
import com.sun.caresyncsystem.utils.MessageUtil;
import com.sun.caresyncsystem.utils.api.AdminApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.sun.caresyncsystem.utils.AppConstants.BCRYPT_STRENGTH;
import static com.sun.caresyncsystem.utils.api.AuthApiPaths.Endpoint.*;
import static com.sun.caresyncsystem.utils.api.UserApiPaths.Endpoint.FULL_REGISTER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {FULL_REGISTER, FULL_LOGIN, FULL_LOGOUT, FULL_VERIFY_TOKEN, FULL_ACTIVATE};

    private final MessageUtil messageUtil;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(messageUtil);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
                requests
                        // add end points with not auth here
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        // add end points for admin here
                        .requestMatchers(AdminApiPaths.BASE_ALL)
                        .hasRole(UserRole.ADMIN.name())
                        .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(exception -> {
            exception.accessDeniedHandler(accessDeniedHandler());
        });
        return http.build();
    }
}
