package com.exampleinyection.clase2parte2.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(com.exampleinyection.clase2parte2..*)")
    public void applicationPackagePointcut() {
        // Pointcut for all methods in application packages.
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String targetClass = joinPoint.getTarget().getClass().getCanonicalName();

        log.debug("before calling {} with params {}", targetClass, Arrays.toString(joinPoint.getArgs()));

        try {
            Object proceeded = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            log.debug("after calling {} with result {} in {} ms", targetClass, proceeded, executionTime);
            return proceeded;
        } catch (Throwable throwable) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.debug("error calling {} after {} ms", targetClass, executionTime, throwable);
            throw throwable;
        }
    }
}

