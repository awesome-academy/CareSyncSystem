package com.sun.caresyncsystem.service;

import java.io.InputStream;

public interface ScheduleService {
    void importSchedules(InputStream inputStream, Long doctorId);
}
