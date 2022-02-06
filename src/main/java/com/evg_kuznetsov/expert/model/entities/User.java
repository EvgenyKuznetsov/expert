package com.evg_kuznetsov.expert.model.entities;

import com.evg_kuznetsov.expert.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;

import static java.util.Collections.checkedList;
import static java.util.Collections.emptyList;

@Entity(name = "user")
@Table(name = "USER")
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

    @Column(name = "FULL_NAME")
    @NotBlank
    @Length(min = 3, max = 255)
    private String fullName;

    @NaturalId
    @Column(name = "EMAIL", unique = true)
    @NotBlank
    @Length(min = 5, max = 255)
    @Email
    private String email;

    @Column(name = "PASSWORD")
    @NotBlank
    @Length(min = 5, max = 255)
    private String password;

    @NaturalId
    @Column(name = "PHONE_NUMBER", unique = true)
    @NotBlank
    @Length(min = 10, max = 10)
    private String phoneNumber;

    @Column(name = "ACTIVE", columnDefinition = "boolean default true")
    @NotNull
    private boolean active;

    private List<Role> roles;

    private List<Order> orders;

    public User(User u) {

        if (u == null) {
            throw new IllegalArgumentException();
        }

        this.id = u.getId();
        this.fullName = u.getFullName();
        this.email = u.getEmail();
        this.password = u.getPassword();
        this.phoneNumber = u.getPhoneNumber();

        Collections<Role> uRoles = u.getRoles();
        if (CollectionUtils.isEmpty(uRoles)) {
            this.roles = emptyList();
        } else this.roles = checkedList(uRoles, Role.class);

        Collections<Order> uOrders = u.getOrders();
        if (CollectionUtils.isEmpty(uOrders)) {
            this.orders = emptyList();
        } else this.orders = checkedList(uOrders, Order.class);

    }
}
