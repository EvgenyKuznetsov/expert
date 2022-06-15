package com.evgKuznetsov.expert.model.entities;

import com.evgKuznetsov.expert.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "role")
@Table()
@NoArgsConstructor
@Getter
@Setter
public class Role extends AbstractEntity {

    public Role(@Nullable Long id, String role) {
        setId(id);
        this.role = role;
    }

    @Column(name = "role", length = 50)
    @NotBlank
    @Length(min = 3, max = 50)
    private String role;

    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore // TODO: 16.02.2022 there's a stack overflow ex
    private Set<User> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role1 = (Role) o;

        return role.equals(role1.role);
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }
}
