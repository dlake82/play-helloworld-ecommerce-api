package com.saysimple.axon.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductCountDecrementedEvent {

    private String orderId;
    private String productId;

    @Override
    public String toString() {
        return "ProductCountDecrementedEvent{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
