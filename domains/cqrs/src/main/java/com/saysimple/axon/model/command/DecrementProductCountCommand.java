package com.saysimple.axon.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
public class DecrementProductCountCommand {
    private @TargetAggregateIdentifier String orderId;
    private String productId;

    @Override
    public String toString() {
        return "DecrementProductCountCommand{" + "orderId='" + orderId + '\'' + ", productId='" + productId + '\'' + '}';
    }
}
