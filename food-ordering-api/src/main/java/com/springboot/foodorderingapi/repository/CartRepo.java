package com.springboot.foodorderingapi.repository;

import com.springboot.foodorderingapi.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
}
