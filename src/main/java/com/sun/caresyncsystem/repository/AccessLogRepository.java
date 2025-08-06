package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
