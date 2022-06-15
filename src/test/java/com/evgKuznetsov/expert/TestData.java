package com.evgKuznetsov.expert;

import com.evgKuznetsov.expert.model.dto.UserTransferObject;
import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.model.entities.User;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class TestData {
    public static Long USER_ID = 1L;
    public static String NEW_FULLNAME = "Charlie Jonson";

    public static Role EXPERT = new Role(1L, "expert");

    public static User USER = new User(USER_ID,
            "Михаил Лермонтов",
            "lermontov@mail.com",
            "lermontov",
            "9272369586",
            true,
            Set.of(EXPERT));

    public static UserTransferObject USER_TO = UserTransferObject.builder()
            .id(USER_ID)
            .fullName(USER.getFullName())
            .phoneNumber(USER.getPhoneNumber())
            .email(USER.getEmail())
            .active(USER.isActive())
            .roles(USER.getRoles())
            .build();

    public static UserTransferObject CHANGED_USER = UserTransferObject.builder()
            .id(USER_ID)
            .fullName(NEW_FULLNAME)
            .phoneNumber(USER.getPhoneNumber())
            .email(USER.getEmail())
            .active(USER.isActive())
            .roles(USER.getRoles())
            .build();

    public static User getNewUser() {
        return new User(null,
                "Bob Dylan",
                "Dylan@yahoo.com",
                "dylan777",
                "9373004715",
                true,
                Set.of(EXPERT));
    }
}
