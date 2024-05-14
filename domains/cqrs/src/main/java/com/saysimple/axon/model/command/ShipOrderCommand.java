package com.saysimple.axon.model.command;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ShipOrderCommand {
    @TargetAggregateIdentifier
    String orderId;
}
