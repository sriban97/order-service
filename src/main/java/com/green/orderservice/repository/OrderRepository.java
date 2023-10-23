package com.green.orderservice.repository;


import com.green.orderservice.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Long> {
}
