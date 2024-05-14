package com.saysimple.axon.model.command;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    private final String orderId;
    private final String productId;
    private final String userId;
    private final Integer qty;
    private final Integer unitPrice;
}
