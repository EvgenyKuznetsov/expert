package com.evg_kuznetsov.expert.model.entities;

import com.evg_kuznetsov.expert.model.AbstractEntity;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "order")
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_generator")
    @SequenceGenerator(name = "order_seq_generator", sequenceName = "order_seq", initialValue = 1000, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "num_incident")
    @NotNull
    @Min(10000000)
    private Integer numberIncident;

    @Column(name = "num_order", columnDefinition = "integer default 0")
    private Integer numberOrder;

    @Column(name = "completion_date")
    @NotNull
    private LocalDateTime completionDate;

    @Setter(AccessLevel.NONE)
    @Column(name = "persist_time")
    private LocalDateTime persistTime;

    @Setter(AccessLevel.NONE)
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @ManyToOne
    @JoinColumn(name = "type_insurance_id")
    private Insurance typeInsurance;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "orders_services",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id")
    )
    private List<Service> services;

    @Column(name = "address")
    @NotBlank
    @Length(min = 5, max = 255)
    private String address;

    @Column(name = "property")
    @NotBlank
    @Length(min = 5, max = 255)
    private String property;

    @Column(name = "desc_property")
    private String descProperty;

    @Column(name = "owner")
    @NotBlank
    @Length(min = 5, max = 255)
    private String owner;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public void prePersist() {
        this.persistTime = LocalDateTime.now();
        super.prePersist();
    }

    @Override
    public void preUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }
}
