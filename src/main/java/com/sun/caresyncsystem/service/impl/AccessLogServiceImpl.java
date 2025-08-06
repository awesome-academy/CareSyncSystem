package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.model.entity.AccessLog;
import com.sun.caresyncsystem.repository.AccessLogRepository;
import com.sun.caresyncsystem.service.AccessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessLogServiceImpl implements AccessLogService {
    private final AccessLogRepository accessLogRepository;

    public void save(AccessLog log) {
        accessLogRepository.save(log);
    }
}
