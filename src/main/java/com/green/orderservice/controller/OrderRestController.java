package com.green.orderservice.controller;

import com.green.orderservice.entity.Order;
import com.green.orderservice.model.OrderResponse;
import com.green.orderservice.model.Payment;
import com.green.orderservice.openfeign.PaymentController;
import com.green.orderservice.repository.OrderRepository;
import io.micrometer.tracing.annotation.NewSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name = "Order", path = "/order")
public class OrderRestController {

    @Autowired
    private Environment environment;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentController paymentController;


    @NewSpan("my-span")
    @PostMapping(name = "Save Order", path = "/save")
    public ResponseEntity<OrderResponse> save(@RequestBody Order order) {
        Order order1 = orderRepository.save(order);
        ResponseEntity<Payment> responseEntity = paymentController.save(Payment.builder().orderId(order.getId()).amount(order.getRate()).build());
        Payment payment = null;
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            payment = responseEntity.getBody();
        }
        return new ResponseEntity<>(OrderResponse.builder().order(order1).payment(payment).build(), HttpStatus.OK);
    }

    @GetMapping(name = "Get Order by ID ", path = "/getById")
    public ResponseEntity<Order> getById(@RequestParam("id") Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        System.out.println(id+""+ order);
        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @GetMapping(name = "Get Order by ID ", path = "/test")
    @ResponseBody
    public List<String> test() {
        return List.of("ABC","AA");
    }


}
