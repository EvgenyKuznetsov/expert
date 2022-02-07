package com.evg_kuznetsov.expert.model.entities;

import com.evg_kuznetsov.expert.model.AbstractEntity;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity(name = "Order")
@Table(name = "ORDERS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_generator")
    @SequenceGenerator(name = "order_seq_generator", sequenceName = "order_seq", initialValue = 1000, allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;


    @Column(name = "NUM_INCIDENT")
    @NotNull
    @Min(10000000)
    private Integer numberIncident;

    @Column(name = "NUM_ORDER", columnDefinition = "integer default 0")
    private Integer numberOrder;

    @Column(name = "COMPLETION_DATE")
    @NotNull
    private LocalDateTime completionDate;

    @Setter(AccessLevel.NONE)
    @Column(name = "LAST_UPDATE")
    private LocalDateTime lastUpdate;

    private String typeInsurance;

    private List<Service> services;

    @Column(name = "ADDRESS")
    @NotBlank
    @Length(min = 5, max = 255)
    private String address;

    @Column(name = "PROPERTY")
    @NotBlank
    @Length(min = 5, max = 255)
    private String property;

    @Column(name = "DESC_PROPERTY")
    private String descProperty;

    @Column(name = "OWNER")
    @NotBlank
    @Length(min = 5, max = 255)
    private String owner;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Override
    public void prePersist() {
        this.setPersistDate();
        super.prePersist();
    }

    @Override
    public void preUpdate() {
        this.setLastUpdateDate();
    }
}
