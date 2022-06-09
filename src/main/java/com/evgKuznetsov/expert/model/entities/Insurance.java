package com.evgKuznetsov.expert.model.entities;

import com.evgKuznetsov.expert.model.AbstractEntity;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name = "insurance")
@Table(name = "insurance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Insurance extends AbstractEntity {

    @Column(name = "insurance_type", unique = true, length = 10)
    @NotBlank
    @Length(max = 10)
    private String insuranceType;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "typeInsurance")
    private List<Order> orders = new ArrayList<>();

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

        Insurance insurance = (Insurance) o;

        return insuranceType.equals(insurance.insuranceType);
    }

    @Override
    public int hashCode() {
        return insuranceType.hashCode();
    }
}