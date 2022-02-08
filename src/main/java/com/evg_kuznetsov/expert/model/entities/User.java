package com.evg_kuznetsov.expert.model.entities;

import com.evg_kuznetsov.expert.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Order> orders = new java.util.ArrayList<>();
}
