package org.atlas.orderservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.orderservice.dto.PaymentEvent;
import org.atlas.orderservice.service.OrderService;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final OrderService orderService;

    @RetryableTopic(
            attempts = "2",
            backOff = @BackOff(delay = 200)
    )
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
            else{
                log.warn("Unknown Event type {}" , paymentEvent.getEventType());
            }
     }

     @DltHandler
    public void processFailedMessages(PaymentEvent paymentEvent) {
            log.info("Processing of message Failed due to internal error  {}", paymentEvent);
     }

}
