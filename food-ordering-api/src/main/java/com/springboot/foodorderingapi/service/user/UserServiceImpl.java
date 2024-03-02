package com.springboot.foodorderingapi.service.user;

import com.springboot.foodorderingapi.entity.User;
import com.springboot.foodorderingapi.jwt.JwtProvider;
import com.springboot.foodorderingapi.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;

    private final JwtProvider jwtProvider;
    @Override
    public User findUserByJwt(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user = findUserByEmail(email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = userRepo.findByEmail(email);
        if (user == null){
            throw new Exception("User not found");
        }
        return user;
    }
}
