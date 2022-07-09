package com.evgKuznetsov.expert.model.dto;

import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.model.validation.constraints.ValidEmail;
import com.evgKuznetsov.expert.model.validation.constraints.ValidFullName;
import com.evgKuznetsov.expert.model.validation.constraints.ValidId;
import com.evgKuznetsov.expert.model.validation.constraints.ValidPhoneNumber;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@NotNull
public class UserTransferObject {

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

    @Valid
    private Set<Role> roles;

}
