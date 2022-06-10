package com.evgKuznetsov.expert.model.dto;

import com.evgKuznetsov.expert.model.entities.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class UserTransferObject {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean active;
    private List<Role> roles;
}
