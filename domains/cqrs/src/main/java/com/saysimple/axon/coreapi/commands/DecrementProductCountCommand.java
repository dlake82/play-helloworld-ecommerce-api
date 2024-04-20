package com.saysimple.axon.coreapi.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
public record DecrementProductCountCommand(@TargetAggregateIdentifier String orderId, String productId) {

    @Override
    public String toString() {
        return "DecrementProductCountCommand{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
