package com.springboot.foodorderingapi.dto;

import com.springboot.foodorderingapi.entity.User;
import com.springboot.foodorderingapi.enums.Role;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private String message;

    private Role role;



}
