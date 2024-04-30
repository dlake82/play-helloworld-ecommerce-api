package com.saysimple.axon.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductAddedEvent {

    private String orderId;
    private String productId;

    @Override
    public String toString() {
        return "ProductAddedEvent{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
