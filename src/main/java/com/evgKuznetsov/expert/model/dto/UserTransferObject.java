package com.evgKuznetsov.expert.model.dto;

import com.evgKuznetsov.expert.model.entities.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserTransferObject {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean active;
    private Set<Role> roles;

}
