package com.evgKuznetsov.expert.service;

import com.evgKuznetsov.expert.model.entities.User;
import com.evgKuznetsov.expert.repository.OrderRepository;
import com.evgKuznetsov.expert.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public List<User> getAllUsers() {
        log.debug("getAllUsers");
        return userRepository.findAll();
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
