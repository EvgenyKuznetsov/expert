package com.evgKuznetsov.expert.model.entities;

import com.evgKuznetsov.expert.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "role")
@Table()
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "global_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "role", length = 50)
    @NotBlank
    @Length(min = 3, max = 50)
    private String role;


    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore // TODO: 16.02.2022 there's a stack overflow ex
    private Set<User> users = new HashSet<>();

    public Set<User> getUsers() {
        if (users.isEmpty()) {
            return Set.of();
        }
        return Collections.unmodifiableSet(this.users);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

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
