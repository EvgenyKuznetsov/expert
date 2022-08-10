package com.evgKuznetsov.expert.web;

import com.evgKuznetsov.expert.model.dto.UserTo;
import com.evgKuznetsov.expert.model.entities.User;
import com.evgKuznetsov.expert.repository.RoleRepository;
import com.evgKuznetsov.expert.repository.UserRepository;
import com.evgKuznetsov.expert.validation.constraints.ValidEmail;
import com.evgKuznetsov.expert.validation.constraints.ValidId;
import com.evgKuznetsov.expert.validation.constraints.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.evgKuznetsov.expert.utils.DataTransferObjectFactory.mergeEntity;
import static com.evgKuznetsov.expert.utils.DataTransferObjectFactory.transformToUTO;

@RestController
@RequestMapping(value = AdminUserController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Validated
public class AdminUserController {

    public static final String URL = "/admin/user";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping(value = "/get_all")
    public List<UserTo> getAll() {
        log.debug("getAll");

        List<User> allUsers = userRepository.findAll();
        return transformToUTO(allUsers);
    }

    @GetMapping(value = "/{id}")
    public UserTo getById(@PathVariable @ValidId Long id) {
        log.debug("getById with an id: {}", id);

        User requestedUser = userRepository.findById(id).orElseThrow();
        return transformToUTO(requestedUser);
    }

    @GetMapping(value = "/get_by_phone_number")
    public UserTo getByPhoneNumber(@RequestParam(value = "phone_number") @ValidPhoneNumber String phone) {
        log.debug("getByPhoneNumber with a phone number: {}", phone);

        User requestedUser = userRepository.getByPhoneNumber(phone).orElseThrow();
        return transformToUTO(requestedUser);
    }

    @GetMapping(value = "/get_by_email")
    public UserTo getByEmail(@RequestParam(value = "email") @ValidEmail String email) {
        log.debug("getByEmail with an email: {}", email);

        User requestedUser = userRepository.getByEmail(email).orElseThrow();
        return transformToUTO(requestedUser);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateUser(@RequestBody @Valid UserTo userTo) {
        log.debug("userTo with data: {}", userTo.toString());

        if (verified(userTo)) {
            User original = userRepository.getById(userTo.getId());
            userRepository.save(mergeEntity(original, userTo));
        }
    }

    private boolean verified(UserTo userTo) {
        Long userId = userTo.getId();
        if (userId == null) {
            log.debug("verified: User id cannot be null: {}", userId);
            return false;
        }
        return userTo.getRoles().stream()
                .allMatch(role -> {
                    Long roleId = role.getId();
                    if (roleId == null) {
                        log.debug("verified: Role id cannot be null: {}", roleId);
                        return false;
                    }
                    return roleRepository.existsById(roleId);
                });
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addNewUser(@RequestBody @Valid User user) {
        log.debug("addNewUser");
        if (!user.isNew()) {
            throw new IllegalArgumentException("User must be new: [expected: User.id == null, actual: User.id == "
                    + user.getId() + "]");
        }
        User newOne = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(newOne.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newOne);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable @ValidId Long id) {
        log.debug("deleteUser with an id: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }


}
