package com.saysimple.axon.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
public class IncrementProductCountCommand {
    @TargetAggregateIdentifier
    private String orderId;
    private String productId;

    @Override
    public String toString() {
        return "IncrementProductCountCommand{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
