package com.evg_kuznetsov.expert.model.entities;

import com.evg_kuznetsov.expert.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity(name = "service")
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Service extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "global_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "service", length = 20)
    @NotBlank
    @Length(min = 3, max = 20)
    private String service;

    @ManyToMany(mappedBy = "services")
    private List<Order> orders;

    @Column(name = "price", scale = 2, precision = 20, columnDefinition = "numeric default 0")
    private BigDecimal price;

    @Column(name = "active", columnDefinition = "boolean default true")
    @NotNull
    private boolean active;

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException();
        }
        if (!this.orders.contains(order)) {
            this.orders.add(order);
        }
    }

    public void removeOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException();
        }
        this.orders.remove(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Service service1 = (Service) o;

        return service.equals(service1.service);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.service);
    }
}