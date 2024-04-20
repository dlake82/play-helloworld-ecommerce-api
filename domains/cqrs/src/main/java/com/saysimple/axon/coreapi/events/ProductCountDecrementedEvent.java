package com.saysimple.axon.coreapi.events;

import lombok.Getter;

@Getter
public record ProductCountDecrementedEvent(String orderId, String productId) {

    @Override
    public String toString() {
        return "ProductCountDecrementedEvent{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
