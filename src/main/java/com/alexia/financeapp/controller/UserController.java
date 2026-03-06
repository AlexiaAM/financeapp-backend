package com.alexia.financeapp.controller;

import com.alexia.financeapp.dto.UserRequest;
import com.alexia.financeapp.entity.User;
import com.alexia.financeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


//tells spring this class handles HTTP requests and that every method returns data (as JSON) directly in the response body
@RestController
//sets the base URL for all methods in this controller. Every endpoint here starts with /api/users
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    //this method handles POST requests (creating data)
    @PostMapping("/register")
    public User register(@RequestBody UserRequest request) {
        return userService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
    }

    private UUID getAuthenticatedUserId() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return user.getId();
    }
}
