package com.sun.caresyncsystem.configuration;

import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.model.enums.UserRole;
import com.sun.caresyncsystem.repository.UserRepository;
import com.sun.caresyncsystem.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppInitConfig {

    private final SecurityProperties securityProperties;
    private final PasswordService passwordService;

    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            if (userRepository.findUserByEmail(securityProperties.getAdmin().getUsername()).isEmpty()) {
                User user = User.builder()
                        .email(securityProperties.getAdmin().getUsername())
                        .password(passwordService.encodePassword(securityProperties.getAdmin().getPassword()))
                        .role(UserRole.ADMIN)
                        .isVerified(true)
                        .isActive(true)
                        .isApproved(true)
                        .build();
                userRepository.save(user);
                log.warn("Admin created");
            }
        };
    }
}
