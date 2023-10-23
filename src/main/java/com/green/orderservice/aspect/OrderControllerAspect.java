package com.green.orderservice.aspect;


import com.green.orderservice.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class OrderControllerAspect {

    @Before(value = "execution(* com.green.orderservice.controller.OrderRestController.*(..))")
    public void before(JoinPoint joinPoint) {
        log.info("{} Being..", joinPoint.getSignature());
    }

    @After(value = "execution(* com.green.orderservice.controller.OrderRestController.*(..))")
    public void after(JoinPoint joinPoint) {
        log.info("{} End", joinPoint.getSignature());
    }

    @AfterThrowing(value = "execution(* com.green.orderservice.controller.OrderRestController.*(..))", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        log.error("{} Exception {}", joinPoint.getSignature(), exception.getMessage());

    }

    @AfterReturning(value = "execution(* com.green.orderservice.controller.OrderRestController.*(..))", returning = "response")
    public void afterThrowing(JoinPoint joinPoint, ResponseEntity<Order> response) {
        log.info("{} Response send Status {} Body {}", joinPoint.getSignature(), response.getStatusCode(),response.getBody());

    }
}
