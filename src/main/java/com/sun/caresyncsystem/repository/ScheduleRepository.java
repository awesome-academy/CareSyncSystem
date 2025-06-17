package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
