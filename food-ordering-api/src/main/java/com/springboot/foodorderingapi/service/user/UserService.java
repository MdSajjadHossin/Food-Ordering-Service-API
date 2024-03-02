package com.springboot.foodorderingapi.service.user;

import com.springboot.foodorderingapi.entity.User;

public interface UserService {

    User findUserByJwt(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;
}
