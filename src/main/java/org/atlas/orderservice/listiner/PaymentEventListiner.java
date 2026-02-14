package org.atlas.orderservice.listiner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.orderservice.dto.PaymentEvent;
import org.atlas.orderservice.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListiner {

    private final OrderService orderService;


    @KafkaListener(topics = "payment-events")
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        log.info("Received payment event: {} for order: {}",
                paymentEvent.getEventType(), paymentEvent.getOrderId());

        if ("PAYMENT_COMPLETED".equals(paymentEvent.getEventType())) {
            orderService.updateOrderStatus(paymentEvent.getOrderId(), "COMPLETED");
            log.info("Order {} marked as COMPLETED", paymentEvent.getOrderId());
        } else if ("PAYMENT_FAILED".equals(paymentEvent.getEventType())) {
            orderService.updateOrderStatus(paymentEvent.getOrderId(), "PAYMENT_FAILED");
            log.info("Order {} marked as PAYMENT_FAILED", paymentEvent.getOrderId());
        }
    }

}
