package com.evg_kuznetsov.expert.model.entities;

import com.evg_kuznetsov.expert.model.AbstractEntity;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @NaturalId
    @Column(name = "num_order", unique = true, columnDefinition = "integer default 0")
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

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "type_insurance_id")
    private Insurance typeInsurance;


    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "orders_services",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id")
    )
    private Set<Service> services = new HashSet<>();

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public void setTypeInsurance(Insurance typeInsurance) {
        if (typeInsurance == null) {
            typeInsurance.removeOrder(this);
        }
        typeInsurance.addOrder(this);
        this.typeInsurance = typeInsurance;
    }

    public void addService(Service service) {
        if (service == null) {
            throw new IllegalArgumentException();
        }
        this.services.add(service);
        service.addOrder(this);
    }

    public void removeService(Service service) {
        if (service == null) {
            throw new IllegalArgumentException();
        }
        this.services.remove(service);
        service.removeOrder(this);
    }

    @Override
    public void prePersist() {
        this.persistTime = LocalDateTime.now();
        super.prePersist();
    }

    @Override
    public void preUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!numberIncident.equals(order.numberIncident)) return false;
        return numberOrder != null ? numberOrder.equals(order.numberOrder) : order.numberOrder == null;
    }

    @Override
    public int hashCode() {
        int result = numberIncident.hashCode();
        result = 31 * result + (numberOrder != null ? numberOrder.hashCode() : 0);
        return result;
    }
}
