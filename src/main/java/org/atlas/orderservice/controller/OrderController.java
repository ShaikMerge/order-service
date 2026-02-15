package org.atlas.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.atlas.orderservice.dto.OrderResponse;
import org.atlas.orderservice.entity.Order;
import org.atlas.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new OrderResponse(savedOrder.getId(), "Processing",
                        "Payment processing. Check status at /api/v1/orders/" + savedOrder.getId()
                        ));
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
         return orderService.getOrderById(orderId);
    }
}
