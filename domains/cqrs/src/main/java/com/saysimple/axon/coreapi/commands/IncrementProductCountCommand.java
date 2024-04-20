package com.saysimple.axon.coreapi.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
public record IncrementProductCountCommand(@TargetAggregateIdentifier String orderId, String productId) {

    @Override
    public String toString() {
        return "IncrementProductCountCommand{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
