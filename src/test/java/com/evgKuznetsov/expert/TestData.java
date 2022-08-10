package com.evgKuznetsov.expert;

import com.evgKuznetsov.expert.model.dto.UserTo;
import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.model.entities.User;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class TestData {
    public static Long USER_ID = 1L;
    public static String NEW_FULLNAME = "Charlie Jonson-Swift jr.";

    public static Role EXPERT = new Role(1L, "expert");

    public static User USER = new User(USER_ID,
            "Михаил Лермонтов",
            "lermontov@mail.com",
            "lermontov",
            "7(927)236-95-86",
            true,
            Set.of(EXPERT));

    public static UserTo USER_TO = UserTo.builder()
            .id(USER_ID)
            .fullName(USER.getFullName())
            .phoneNumber(USER.getPhoneNumber())
            .email(USER.getEmail())
            .active(USER.isActive())
            .roles(USER.getRoles())
            .build();

    public static UserTo CHANGED_USER = UserTo.builder()
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
                "7(937)300-47-15",
                true,
                Set.of(EXPERT));
    }

    public static String getSuperLongPhone() {
        String[] email = USER.getEmail().split("@");
        StringBuilder sb = new StringBuilder(email[0]);

        sb.append("f".repeat(150));

        sb.append("@").append(email[1]);
        return sb.toString();
    }
}
