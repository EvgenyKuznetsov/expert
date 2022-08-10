package com.evgKuznetsov.expert.model.entities;

import com.evgKuznetsov.expert.model.AbstractEntity;
import com.evgKuznetsov.expert.validation.constraints.ValidEmail;
import com.evgKuznetsov.expert.validation.constraints.ValidFullName;
import com.evgKuznetsov.expert.validation.constraints.ValidPhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity(name = "user")
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class User extends AbstractEntity {

    @Column(name = "full_name")
    @ValidFullName
    private String fullName;

    @Column(name = "email", unique = true)
    @ValidEmail
    private String email;

    @Column(name = "password")
    @NotBlank
    @Length(min = 5, max = 255)
    private String password;

    @Column(name = "phone_number", unique = true)
    @ValidPhoneNumber
    private String phoneNumber;

    @Column(name = "active", columnDefinition = "boolean default true")
    @NotNull
    private boolean active;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @Valid
    private Set<Role> roles = new HashSet<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore // TODO: 16.02.2022 there's a stack overflow ex
    private List<Order> orders = new ArrayList<>();

    public User(@Nullable Long id, String fullName, String email, String password, String phoneNumber, boolean active, Set<Role> roles) {
        setId(id);
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        if (this.roles.isEmpty()) {
            return Set.of();
        }
        return Collections.unmodifiableSet(this.roles);
    }

    public void addRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException();
        }
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException();
        }
        if (this.roles.remove(role)) {
            role.getUsers().remove(this);
        }
    }

    public void clearRoles() {
        this.roles.forEach(x -> x.removeUser(this));
        this.roles.clear();
    }

    public List<Order> getOrders() {
        if (this.orders.isEmpty()) {
            return List.of();
        }
        return Collections.unmodifiableList(this.orders);
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException();
        }
        if (!this.orders.contains(order)) {
            this.orders.add(order);
        }
        order.setUser(this);
    }

    public void removeOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException();
        }
        this.orders.remove(order);
        order.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.email);
    }

    @Override
    public String toString() {
        return "[id: " + this.getId()
                + ", fullName: " + this.fullName
                + ", email: " + this.email
                + ", phoneNumber: " + this.phoneNumber
                + ", active: " + this.active + "]";
    }
}
