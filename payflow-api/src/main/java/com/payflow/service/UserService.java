package com.payflow.service;

import com.payflow.entity.User;
import com.payflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    /*
     * At startup, Spring scans for classes annotated with @Service, @Repository, etc.,
     * creates instances (beans) of them, and places them in the application context (IoC container).
     * When it instantiates UserService, it sees this constructor (and the @Autowired annotation)
     * and automatically injects the appropriate UserRepository bean into this service.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUpiId(String upiId) {
        return userRepository.findByUpiId(upiId).orElse(null);
    }
}
