package com.evgKuznetsov.expert.web;

import com.evgKuznetsov.expert.model.dto.UserTransferObject;
import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.model.entities.User;
import com.evgKuznetsov.expert.repository.RoleRepository;
import com.evgKuznetsov.expert.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminUserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping(value = "/{id}")
    public User getById(@PathVariable Long id) {
        log.debug("getById with an id: {}", id);
        return userRepository.findById(id).orElseThrow();
    }

    @GetMapping(value = "/get_by_phone_number")
    public User getByPhoneNumber(@RequestParam(value = "phone_number") String phone) {
        log.debug("getByPhoneNumber with a phone number: {}", phone);
        return userRepository.getByPhoneNumber(phone).orElseThrow();
    }

    @GetMapping(value = "/get_by_email")
    public User getByEmail(@RequestParam(value = "email") String email) {
        log.debug("getByEmail with an email: {}", email);
        return userRepository.getByEmail(email).orElseThrow();
    }

    @GetMapping(value = "/get_all")
    public List<User> getAll() {
        log.debug("getAll");
        return userRepository.findAll();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody UserTransferObject userTo) {
        log.debug("updateUser with data: {}", userTo.toString());
        Long id = userTo.getId();
        User user = userRepository.findById(id).orElseThrow();
        for (Role role : userTo.getRoles()) {
            assert role.getId() != null;
            if (!roleRepository.existsById(role.getId())) {
                log.debug("updateUser: the passed role with id: {}, isn't exist", role.getId());
                throw new IllegalArgumentException();
            }
            user.addRole(role);
        }
        user.setFullName(userTo.getFullName());
        user.setEmail(userTo.getEmail());
        user.setPhoneNumber(userTo.getPhoneNumber());
        user.setActive(userTo.isActive());
        userRepository.save(user);
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addNewUser(@RequestBody User user) {
        log.debug("addNewUser");
        if (user.isNew()) {
            userRepository.save(user);
        }
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.debug("deleteUser with an id: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }


}
