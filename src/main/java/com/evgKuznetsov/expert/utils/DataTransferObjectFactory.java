package com.evgKuznetsov.expert.utils;

import com.evgKuznetsov.expert.model.dto.UserTo;
import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.model.entities.User;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DataTransferObjectFactory {

    public static UserTo transformToUTO(@NotNull User uto) {
        return UserTo.builder()
                .id(uto.getId())
                .fullName(uto.getFullName())
                .email(uto.getEmail())
                .phoneNumber(uto.getPhoneNumber())
                .active(uto.isActive())
                .roles(uto.getRoles())
                .build();
    }

    public static List<UserTo> transformToUTO(@NotNull List<User> list) {
        return list.stream()
                .map(DataTransferObjectFactory::transformToUTO)
                .collect(Collectors.toList());
    }

    public static User mergeEntity(User original, UserTo corrected) {
        original.setFullName(corrected.getFullName());
        original.setEmail(corrected.getEmail());
        original.setPhoneNumber(corrected.getPhoneNumber());
        original.setActive(corrected.isActive());

        if (corrected.getRoles().isEmpty()) {
            original.clearRoles();
        }

        for (Role r : corrected.getRoles()) {
            original.clearRoles();
            original.addRole(r);
        }
        return original;
    }
}
