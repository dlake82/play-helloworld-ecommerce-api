package com.saysimple.axon.coreapi.events;

import lombok.Getter;

@Getter
public record ProductAddedEvent(String orderId, String productId) {

    @Override
    public String toString() {
        return "ProductAddedEvent{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
