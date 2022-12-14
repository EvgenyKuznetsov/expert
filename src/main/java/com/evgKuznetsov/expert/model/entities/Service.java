package com.evgKuznetsov.expert.model.entities;

import com.evgKuznetsov.expert.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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
public class Service extends AbstractEntity {

    @Column(name = "service", length = 50)
    @NotBlank
    @Length(min = 3, max = 50)
    private String service;

    @ManyToMany(mappedBy = "services")
    @JsonIgnore // TODO: 16.02.2022 there is a stack overflow ex
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