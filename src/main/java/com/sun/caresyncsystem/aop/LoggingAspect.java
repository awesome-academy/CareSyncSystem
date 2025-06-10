package com.sun.caresyncsystem.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.sun.caresyncsystem..*.*(..))")
    public void exceptionHandlerMethods() {}

    @AfterThrowing(pointcut = "exceptionHandlerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("❌ Exception in {}: {}", methodName, ex.getMessage(), ex);
    }
}
