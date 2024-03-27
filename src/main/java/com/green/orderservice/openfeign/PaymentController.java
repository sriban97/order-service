package com.green.orderservice.openfeign;

import com.green.orderservice.model.Payment;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.CompletableFuture;

@FeignClient(name = "payment-service", path = "/payment")
public interface PaymentController {

    @CircuitBreaker(name = "payment-service", fallbackMethod = "fallbackCircuitBreaker")
    @Retry(name = "payment-service")
//    @RateLimiter(name = "consumer-service", fallbackMethod = "fallbackRateLimiter" )
//    @TimeLimiter(name = "consumer-service")
//    @Bulkhead(name = "consumer-service", fallbackMethod = "getBulkhead" )
    @PostMapping(name = "Save Payment", path = "/save")
    @Async
    CompletableFuture<ResponseEntity<Payment>> save(@RequestBody Payment payment);


    default ResponseEntity<Payment> fallbackCircuitBreaker(Payment payment ,Exception ex) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    default ResponseEntity<Payment> fallbackRetry(Payment payment ,Exception ex) {
        return new ResponseEntity<>(null, HttpStatus.GATEWAY_TIMEOUT);
    }


//    default String getRateLimiter(Exception ex) {
//        return "RateLimiter Exception : " + ex.getMessage();
//    }
//    default String getTimeLimiter(Exception ex) {
//        return "TimeLimiter Exception : " + ex.getMessage();
//    }
//    default String getBulkhead(Exception ex) {
//        return "TimeLimiter Exception : " + ex.getMessage();
//    }
}
