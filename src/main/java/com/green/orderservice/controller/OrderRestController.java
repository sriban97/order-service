package com.green.orderservice.controller;

import com.green.orderservice.entity.Order;
import com.green.orderservice.model.OrderResponse;
import com.green.orderservice.model.Payment;
import com.green.orderservice.openfeign.PaymentController;
import com.green.orderservice.producer.PaymentProducer;
import com.green.orderservice.repository.OrderRepository;
import com.green.orderservice.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping(name = "Order", path = "/order")
@Slf4j
public class OrderRestController {

    @Autowired
    private Environment environment;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentProducer paymentProducer;
    @Autowired
    private PaymentController paymentController;


    @PostMapping(name = "Save Order", path = "/save")
    public ResponseEntity<OrderResponse> save(@RequestBody Order order, @RequestHeader HttpHeaders header) {
        var LOG_NAME = "save";
        var SSO = header.get(Constant.Header.SSO_ID);

        log.info("{} SSO {} Begin...", SSO, LOG_NAME);

        Order order1 = orderRepository.save(order);
        AtomicReference<Payment> atomicReference = new AtomicReference<>();
        CompletableFuture<ResponseEntity<Payment>> completableFuture = paymentController.save(Payment.builder().orderId(order.getId()).amount(order.getRate()).build());
        completableFuture.whenCompleteAsync((response, error) -> {
            log.info("{} response {},error {}", LOG_NAME, response, error.toString());
            if (!ObjectUtils.isEmpty(error)) {
                log.error("{} error {}", LOG_NAME, error.getMessage());
            } else if (!ObjectUtils.isEmpty(response)) {
                if (response.getStatusCode() == HttpStatus.OK) {
                    atomicReference.set(response.getBody());
                    log.info("{} atomicReference {}", LOG_NAME, atomicReference);
                } else {
                    log.info("{} status code {}", LOG_NAME, response.getStatusCode());
                }
            }
        });

        log.info("{} SSO {} End.", SSO, LOG_NAME);
        return new ResponseEntity<>(OrderResponse.builder().order(order1).payment(atomicReference.get()).build(), HttpStatus.OK);
    }

    @PostMapping(name = "Save Order", path = "/save/v1")
    public ResponseEntity<OrderResponse> saveV1(@RequestBody Order order) {
        var LOG_NAME = "saveV1";
        log.info("{} Begin...", LOG_NAME);
        Order order1 = orderRepository.save(order);
        paymentProducer.pushToPayment(Payment.builder().orderId(order.getId()).amount(order.getRate()).build());
        log.info("{} End.", LOG_NAME);
        return new ResponseEntity<>(OrderResponse.builder().order(order1).payment(null).build(), HttpStatus.OK);
    }

    @GetMapping(name = "Get Order by ID ", path = "/getById")
    public ResponseEntity<Order> getById(@RequestParam("id") Long id) {
        var LOG_NAME = "getById";

        log.info("{} Begin...", LOG_NAME);

        log.info("{} id {}", LOG_NAME, id);
        Order order = orderRepository.findById(id).orElse(null);
        log.info("{} order {}", LOG_NAME, order);

        log.info("{} End.", LOG_NAME);
        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @GetMapping(name = "Get Order by ID ", path = "/test")
    @ResponseBody
    public List<String> test() {
        return List.of("ABC", "AA");
    }


}
