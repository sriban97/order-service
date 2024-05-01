package com.green.orderservice.repository;


import com.green.orderservice.entity.Order;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends CrudRepository<Order, Long> {
    @Procedure("test")
    Integer explicitlyNamedPlus1inout(@Param("arg") Integer arg);
}
