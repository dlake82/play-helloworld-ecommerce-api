package com.saysimple.axon.vo;

import com.saysimple.axon.aggregate.OrderStatus;

public enum OrderStatusResponse {
    CREATED, CONFIRMED, SHIPPED, UNKNOWN;

    static OrderStatusResponse toResponse(OrderStatus status) {
        for (OrderStatusResponse response : values()) {
            if (response.toString()
                    .equals(status.toString())) {
                return response;
            }
        }
        return UNKNOWN;
    }
}
