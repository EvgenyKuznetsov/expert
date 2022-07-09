package com.evgKuznetsov.expert.web;

import com.evgKuznetsov.expert.model.dto.UserTransferObject;
import com.evgKuznetsov.expert.model.entities.User;
import com.evgKuznetsov.expert.model.validation.constraints.ValidEmail;
import com.evgKuznetsov.expert.model.validation.constraints.ValidId;
import com.evgKuznetsov.expert.model.validation.constraints.ValidPhoneNumber;
import com.evgKuznetsov.expert.repository.RoleRepository;
import com.evgKuznetsov.expert.repository.UserRepository;
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

    @GetMapping(value = "/{id}")
    public UserTransferObject getById(@PathVariable @ValidId Long id) {
        log.debug("getById with an id: {}", id);

        User requestedUser = userRepository.findById(id).orElseThrow();
        return transformToUTO(requestedUser);
    }

    @GetMapping(value = "/get_by_phone_number")
    public User getByPhoneNumber(@RequestParam(value = "phone_number") @ValidPhoneNumber String phone) {
        log.debug("getByPhoneNumber with a phone number: {}", phone);
        return userRepository.getByPhoneNumber(phone).orElseThrow();
    }

    @GetMapping(value = "/get_by_email")
    public User getByEmail(@RequestParam(value = "email") @ValidEmail String email) {
        log.debug("getByEmail with an email: {}", email);

        return userRepository.getByEmail(email).orElseThrow();
    }

    @GetMapping(value = "/get_all")
    public List<UserTransferObject> getAll() {
        log.debug("getAll");

        List<User> allUsers = userRepository.findAll();
        return transformToUTO(allUsers);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateUser(@RequestBody @Valid UserTransferObject userTo) {
        log.debug("updateUser with data: {}", userTo.toString());

        if (verified(userTo)) {
            User original = userRepository.getById(userTo.getId());
            userRepository.save(mergeEntity(original, userTo));
        }
    }

    private boolean verified(UserTransferObject userTo) {
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
            throw new IllegalArgumentException("User must be new: [expected: User.id == null, actual: User.id == " + user.getId());
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
