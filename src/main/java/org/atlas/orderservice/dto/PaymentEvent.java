package org.atlas.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private Long paymentId;
    private Long orderId;
    private String customerId;
    private BigDecimal amount;
    private String status; // COMPLETED or FAILED
    private String transactionId;
    private String timestamp;
    private String eventType; // PAYMENT_COMPLETED, PAYMENT_FAILED
}