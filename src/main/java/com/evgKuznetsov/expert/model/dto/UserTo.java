package com.evgKuznetsov.expert.model.dto;

import com.evgKuznetsov.expert.model.IdAuditable;
import com.evgKuznetsov.expert.model.entities.Role;
import com.evgKuznetsov.expert.validation.constraints.ValidEmail;
import com.evgKuznetsov.expert.validation.constraints.ValidFullName;
import com.evgKuznetsov.expert.validation.constraints.ValidId;
import com.evgKuznetsov.expert.validation.constraints.ValidPhoneNumber;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTo implements IdAuditable {

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

    private Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTo userTo = (UserTo) o;

        if (id != null ? !id.equals(userTo.id) : userTo.id != null) return false;
        if (!fullName.equals(userTo.fullName)) return false;
        if (!email.equals(userTo.email)) return false;
        return phoneNumber.equals(userTo.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + fullName.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "[" +
                "id: " + id +
                ", fullName: " + fullName +
                ", email: " + email +
                ", phoneNumber: " + phoneNumber +
                ", active: " + active +
                ", roles: " + roles +
                "]";
    }
}
