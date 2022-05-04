package com.evgKuznetsov.expert.controller;

import com.evgKuznetsov.expert.model.entities.User;
import com.evgKuznetsov.expert.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = AdminController.ADMIN_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminController {

    public static final String ADMIN_PATH = "/admin";
    private final AdminService adminService;

    @GetMapping("/users")
    public List<User> getAll() {
        log.debug("getAll");
        return adminService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getOne(@PathVariable Long id) {
        log.debug("getOne with id: {}", id);
        return adminService.getUserById(id);
    }
}
