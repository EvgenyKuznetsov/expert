package com.evgKuznetsov.expert.web;

import com.evgKuznetsov.expert.exception.IllegalRequestDataException;
import com.evgKuznetsov.expert.model.dto.UserTo;
import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.model.entities.User;
import com.evgKuznetsov.expert.repository.RoleRepository;
import com.evgKuznetsov.expert.repository.UserRepository;
import com.evgKuznetsov.expert.validation.constraints.ValidId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;

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
    private final MessageSource messageSource;

    @GetMapping(value = "/get_all")
    public List<UserTo> getAll() {
        log.debug("getAll:");

        List<User> allUsers = userRepository.findAll();
        return transformToUTO(allUsers);
    }

    @GetMapping(value = "/{id}")
    public UserTo getById(@PathVariable @ValidId Long id) {
        log.debug("getById with the id: {}", id);
        return transformToUTO(userRepository.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateUser(@PathVariable @ValidId Long id, @RequestBody @Valid UserTo userTo, WebRequest request) {
        log.debug("updateUser with the id: {}, body: {}", id, userTo.toString());
        Locale loc = request.getLocale();

        User user = userRepository.getById(id);
        checkUserTo(id, userTo, loc);

        userRepository.save(mergeEntity(user, userTo));
    }

    private void checkUserTo(Long id, UserTo userTo, Locale loc) {
        log.debug("AdminUserController.checkUserTo");
        if (!Objects.equals(id, userTo.getId())) {
            String m = messageSource.getMessage("validation.data.conflict", null, loc);
            throw new IllegalRequestDataException(String.format(m, "user.id"));
        }
        checkRoles(userTo.getRoles(), loc);
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserTo> addNewUser(@RequestBody @Valid User user, WebRequest request) {
        log.debug("addNewUser: {}", user);
        Locale loc = request.getLocale();

        if (!user.isNew()) {
            String m = messageSource.getMessage("validation.data.conflict", null, loc);
            throw new IllegalRequestDataException(String.format(m, "user.id"));
        }

        checkRoles(user.getRoles(), loc);

        UserTo newOne = transformToUTO(userRepository.save(user));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(newOne.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newOne);
    }

    private void checkRoles(Set<Role> roles, Locale loc) {
        log.debug("AdminUserController.checkRoles");
        for (Role rPassed : roles) {
            Assert.notNull(rPassed.getId(), "Role's id must be not null!");
            Optional<Role> op = roleRepository.findById(rPassed.getId());
            if (op.isEmpty()) {
                String m = messageSource.getMessage("validation.data.conflict", null, loc);
                throw new IllegalRequestDataException(String.format(m, "user.role"));
            }
            Role rExist = op.get();
            String rExistName = rExist.getRole(), rPassedName = rPassed.getRole();
            if (!Objects.equals(rExistName, rPassedName)) {
                String m = messageSource.getMessage("validation.data.conflict", null, loc);
                throw new IllegalRequestDataException(String.format(m, "user.role"));
            }
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable @ValidId Long id) {
        log.debug("deleteUser with the id: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }


}
