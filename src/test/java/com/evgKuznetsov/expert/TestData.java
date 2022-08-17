package com.evgKuznetsov.expert;

import com.evgKuznetsov.expert.model.dto.UserTo;
import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.model.entities.User;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class TestData {
    public static final Long USER_ID = 1L;
    public static final String NEW_FULLNAME = "Charlie Jonson-Swift jr.";

    public static final Role EXPERT = new Role(1L, "expert");
    public static final Role ADMIN = new Role(2L, "admin");

    public static User USER = new User(USER_ID,
            "Михаил Лермонтов",
            "lermontov@mail.com",
            "lermontov",
            "7(927)236-95-86",
            true,
            Set.of(EXPERT));

    public static User getUser() {
        return new User(USER_ID,
                "Михаил Лермонтов",
                "lermontov@mail.com",
                "lermontov",
                "7(927)236-95-86",
                true,
                Set.of(EXPERT));
    }

    public static UserTo getUserTo() {
        return UserTo.builder()
                .id(USER_ID)
                .fullName(USER.getFullName())
                .phoneNumber(USER.getPhoneNumber())
                .email(USER.getEmail())
                .active(USER.isActive())
                .roles(USER.getRoles())
                .build();
    }

    public static UserTo getChangedUser() {
        return UserTo.builder()
                .id(USER_ID)
                .fullName(NEW_FULLNAME)
                .phoneNumber(USER.getPhoneNumber())
                .email(USER.getEmail())
                .active(USER.isActive())
                .roles(USER.getRoles())
                .build();
    }

    public static User getNewUser() {
        Set<Role> roles = new HashSet<>();
        roles.add(EXPERT);
        return new User(null,
                "Bob Dylan",
                "Dylan@yahoo.com",
                "dylan777",
                "7(937)300-47-15",
                true,
                roles);
    }

    public static String getSuperLongEmail() {
        String[] email = USER.getEmail().split("@");
        return email[0] + "f".repeat(45) + "@" + email[1];
    }
}
