package com.evgKuznetsov.expert.model.dto;

import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.repository.RoleRepository;
import com.evgKuznetsov.expert.validation.constraints.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@NotNull
public class UserTo {

    @ValidId
    private Long id;

    @ValidFullName
    private String fullName;

    @ValidEmail
    private String email;

    @ValidPhoneNumber
    private String phoneNumber;

    @NotNull
    private boolean active;

    private Set<@isExist(repository = RoleRepository.class) Role> roles;
}
