package com.springboot.foodorderingapi.controller;

import com.springboot.foodorderingapi.dto.AuthResponse;
import com.springboot.foodorderingapi.dto.LoginRequest;
import com.springboot.foodorderingapi.entity.Cart;
import com.springboot.foodorderingapi.entity.User;
import com.springboot.foodorderingapi.enums.Role;
import com.springboot.foodorderingapi.jwt.JwtProvider;
import com.springboot.foodorderingapi.repository.CartRepo;
import com.springboot.foodorderingapi.repository.UserRepo;
import com.springboot.foodorderingapi.service.jwt.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.DataInput;
import java.util.Collection;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final CartRepo cartRepo;

    @PostMapping("/signup")
    private ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user){

        User isEmailExist = userRepo.findByEmail(user.getEmail());
        if(isEmailExist !=null){
            throw new BadCredentialsException("Email is already exist");
        }
        User createuser = new User();
        createuser.setName(user.getName());
        createuser.setEmail(user.getEmail());
        createuser.setPassword(passwordEncoder.encode(user.getPassword()));
        createuser.setRole(user.getRole());

        User savedUser = userRepo.save(createuser);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepo.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Registration successful");
        authResponse.setRole(savedUser.getRole()
        );
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authentication(email, password);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login successful");
        authResponse.setRole(Role.valueOf(role));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authentication(String email, String password) {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if(userDetails == null){
            throw new BadCredentialsException("Invalid user name.......");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Password did not match......");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
