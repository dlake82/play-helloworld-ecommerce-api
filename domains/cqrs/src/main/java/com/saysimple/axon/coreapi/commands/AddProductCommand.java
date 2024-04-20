package com.saysimple.axon.coreapi.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
public record AddProductCommand(@TargetAggregateIdentifier String orderId, String productId) {

    @Override
    public String toString() {
        return "AddProductCommand{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
