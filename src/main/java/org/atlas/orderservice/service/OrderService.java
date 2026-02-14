package org.atlas.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.orderservice.dto.OrderEvent;
import org.atlas.orderservice.entity.Order;
import org.atlas.orderservice.repository.OrderJpaRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {


    private final OrderJpaRepository orderJpaRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Transactional
    public Order createOrder(Order order) {
        Order createdOrder = orderJpaRepository.save(order);
        OrderEvent orderEvent = OrderEvent.builder()
                .amount(createdOrder.getAmount())
                .orderId(createdOrder.getId())
                .customerId(createdOrder.getCustomerId())
                .status(createdOrder.getStatus())
                .productName(createdOrder.getProductName())
                .build();
        kafkaTemplate.send("order-events", orderEvent);
        log.info("Order created and event published: {}", orderEvent);
        return createdOrder;
    }

    public void getOrderById(Long id) {
        orderJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
}
