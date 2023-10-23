package com.green.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "order_booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ord_id")
    private long id;

    @Column(name = "ord_item_name")
    private String itemName;

    @Column(name = "ord_amount")
    private double rate;

}
