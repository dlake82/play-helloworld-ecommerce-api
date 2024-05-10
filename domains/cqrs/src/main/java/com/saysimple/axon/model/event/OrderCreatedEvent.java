package com.saysimple.axon.model.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderCreatedEvent {
    private final String orderId;
    private final String productId;
    private final String userId;
}
