package org.atlas.orderservice.repository;


import org.atlas.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository  extends JpaRepository<Order, Long> {
}
