package com.saysimple.axon.model.command;

import com.saysimple.axon.aggregate.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ShipOrderCommand {
    private @TargetAggregateIdentifier String orderId;
}
