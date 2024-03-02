package com.springboot.foodorderingapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data

public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User customer;

    @JsonIgnore
    @ManyToOne()
    private Restaurant restaurant;

    private Long totalAmount;

    private String orderStatus;

    private Date orderedAt;

    @ManyToOne
    private Address deliveryAddress;

    @OneToMany
    private List<OrderItems> items;

    private int totalItem;

    private Long totalPrice;

}
