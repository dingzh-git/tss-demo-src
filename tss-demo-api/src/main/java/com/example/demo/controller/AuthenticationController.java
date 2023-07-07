package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.security.JwtResponse;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {
        try {
            User user = userService.getUser(loginRequest.getId());
            if(user == null) {
                throw new BadCredentialsException("Invalid username or password");
            }

            if(!user.getPassword().equals(loginRequest.getPassword())) {
                throw new BadCredentialsException("Invalid username or password");
            }
            
            //UserDetails userDetails = new CustomUserDetails(user);
            final String token = jwtUtil.generateToken(user.getId());

            return ResponseEntity.ok(new JwtResponse(user, token));

        } catch (Exception e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

}
