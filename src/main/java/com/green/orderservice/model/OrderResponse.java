package com.green.orderservice.model;

import com.green.orderservice.entity.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Order order;
    private Payment payment;
}
