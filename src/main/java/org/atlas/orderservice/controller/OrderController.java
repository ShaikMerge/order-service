package org.atlas.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.orderservice.dto.OrderResponse;
import org.atlas.orderservice.dto.Userinfo;
import org.atlas.orderservice.entity.Order;
import org.atlas.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody Order order,
            @RequestAttribute("userId") Long userId,
            @RequestAttribute("email") String email) {
        log.info("Creating order for authenticated user: {} (ID: {})", email, userId);
        order.setCustomerId(userId.toString());
        Order createdOrder = orderService.createOrder(order);
        OrderResponse response = OrderResponse.builder()
                .orderId(createdOrder.getId())
                .status(createdOrder.getStatus())
                .message("Order created successfully. Payment processing in background.")
                .statusCheckUrl("/api/v1/orders/" + createdOrder.getId())
                .build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
         return orderService.getOrderById(orderId);
    }

    @GetMapping("/me")
    public ResponseEntity<Userinfo> getCurrentUser(
            @RequestAttribute("userId") Long userId,
            @RequestAttribute("email") String email,
            @RequestAttribute("role") String role,
            @RequestAttribute("fullName") String fullName) {

        Userinfo userInfo = Userinfo.builder()
                .userId(userId)
                .email(email)
                .fullName(fullName)
                .role(role)
                .build();
        return ResponseEntity.ok(userInfo);
    }


}


