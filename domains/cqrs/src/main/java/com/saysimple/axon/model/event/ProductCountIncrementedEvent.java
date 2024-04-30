package com.saysimple.axon.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductCountIncrementedEvent{

    private String orderId;
    private String productId;

    @Override
    public String toString() {
        return "ProductCountIncrementedEvent{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
