package com.saysimple.axon.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductRemovedEvent {

    private String orderId;
    private String productId;

    @Override
    public String toString() {
        return "ProductRemovedEvent{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
