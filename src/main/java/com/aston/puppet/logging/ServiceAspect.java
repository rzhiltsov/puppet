package com.aston.puppet.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ServiceAspect {

    @Around("execution(public * *..service..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodDescription = joinPoint.getSignature().toString();
        log.info("Calling method {}", methodDescription);
        log.debug("Calling method {} with arguments {}", methodDescription, joinPoint.getArgs());
        try {
            Object result = joinPoint.proceed();
            log.debug("Returning from method {} with value {}", methodDescription, result);
            return result;
        } catch (Throwable ex) {
            log.error("Method {} threw {}", methodDescription, ex.toString());
            throw ex;
        }
    }
}
