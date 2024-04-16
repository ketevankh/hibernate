package com.example.task_hibernate.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//FIXME I didn't find required logging - 17. Two levels of logging
//You can try to implement something using the example below

@Aspect
@Slf4j(topic = "info")
@Component
public class AspectLogger {

    @Pointcut("within(com.example.task_hibernate.repository.*)")
    public void daoClasses() {
    }

    @Pointcut("within(com.example.task_hibernate.service.*)")
    public void serviceClasses() {
    }

    @Pointcut("within(com.example.task_hibernate.controller.*)")
    public void controllerClasses() {
    }

    @Around("daoClasses() || serviceClasses() || controllerClasses()")
    public Object proceedLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed;

        try {
            String requestIdentity = ""; //FIXME create some class or method  IdentityHolder.getIdentity();
            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();
            long startTimeMillis = System.currentTimeMillis();

            log.info("{} - Start executing: {}.{}({})", requestIdentity, className, methodName, Arrays.toString(args));
            proceed = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTimeMillis;
            log.info("{} - {}.{} finished by: {} ms. With result: {}", requestIdentity, className, methodName, executionTime, proceed == null ? "null" : proceed.toString());

        } catch (Exception controllableException) { //FIXME here can be specifies some special exception
            log.error(controllableException.getMessage());
            throw controllableException;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            throw throwable;
        }
        return proceed;
    }
}
