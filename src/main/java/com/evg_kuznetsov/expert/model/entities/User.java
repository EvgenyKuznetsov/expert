package com.evg_kuznetsov.expert.model.entities;

import com.evg_kuznetsov.expert.model.AbstractEntity;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity(name = "user")
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "global_seq", initialValue = 1000, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "full_name")
    @NotBlank
    @Length(min = 3, max = 255)
    private String fullName;

    @NaturalId
    @Column(name = "email", unique = true)
    @NotBlank
    @Length(min = 5, max = 255)
    @Email
    private String email;

    @Column(name = "password")
    @NotBlank
    @Length(min = 5, max = 255)
    private String password;

    @NaturalId
    @Column(name = "phone_number", unique = true)
    @NotBlank
    @Length(min = 10, max = 10)
    private String phoneNumber;

    @Column(name = "active", columnDefinition = "boolean default true")
    @NotNull
    private boolean active;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();


    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

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
        role.addUser(this);
    }

    public void removeRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException();
        }
        if (this.roles.remove(role)) {
            role.removeUser(this);
        }
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
}
