package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsUserByEmail(String email);

    Optional<User> findUserByEmail(String email);

    Page<User> findByRoleIn(List<UserRole> roles, Pageable pageable);
}
