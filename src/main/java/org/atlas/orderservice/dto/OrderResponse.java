package org.atlas.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String status;
    private String message;

}
