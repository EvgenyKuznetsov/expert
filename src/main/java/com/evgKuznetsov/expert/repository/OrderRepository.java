package com.evgKuznetsov.expert.repository;

import com.evgKuznetsov.expert.model.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
