package com.payflow.controller;

import com.payflow.entity.User;
import com.payflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User registerUser(@RequestBody User user) {
        System.out.println("Received User object (WITH @RequestBody): name=" + user.getName() + ", upiId=" + user.getUpiId() + ", balance=" + user.getBalance());
        return userService.registerUser(user);
    }
    
    // Created purely for demonstration purposes as required by Task 5
    @PostMapping("/test-no-body")
    public User registerUserWithoutBody(User user) {
        System.out.println("Received User object (WITHOUT @RequestBody): name=" + user.getName() + ", upiId=" + user.getUpiId() + ", balance=" + user.getBalance());
        return userService.registerUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/upi/{upiId}")
    public User getUserByUpiId(@PathVariable String upiId) {
        return userService.findByUpiId(upiId);
    }
}
