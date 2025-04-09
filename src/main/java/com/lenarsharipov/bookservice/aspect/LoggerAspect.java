package com.lenarsharipov.bookservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Pointcut("execution(* com.lenarsharipov.bookservice..*(..)) && " +
            "!execution(* com.lenarsharipov.bookservice.controller.BookController.handleAllExceptions(..))")
    public void loggableMethods() {}

    @Around("loggableMethods()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("Executing {}", methodName);

        try {
            Object result = joinPoint.proceed();
            log.info("Successfully executed {}", methodName);
            return result;
        } catch (Exception e) {
            // Добавляем сообщение из исключения в лог
            log.error("Exception in {}: {}", methodName, e.getMessage());
            throw e;
        }
    }
}