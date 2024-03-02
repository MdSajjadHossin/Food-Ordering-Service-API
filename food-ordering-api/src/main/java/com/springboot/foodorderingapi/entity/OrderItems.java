package com.springboot.foodorderingapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class OrderItems {

    @ManyToOne
    private Food food;

    private int quantity;

    private Long totalPrice;

    private List<String> ingredients;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



}
