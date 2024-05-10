package com.saysimple.axon.aggregate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Getter
@EqualsAndHashCode
@ToString
@Aggregate(snapshotTriggerDefinition = "orderAggregateSnapshotTriggerDefinition")
public class OrderAggregate {

    @AggregateIdentifier
    private final String orderId;
    private final String productId;
    private final String userId;
    private OrderStatus orderStatus;

    public OrderAggregate(String orderId, String productId, String userId) {
        this.orderId = orderId;
        this.productId = productId;
        this.userId = userId;
        orderStatus = OrderStatus.CREATED;
    }

    public void setOrderConfirmed() {
        this.orderStatus = OrderStatus.CONFIRMED;
    }

    public void setOrderShipped() {
        this.orderStatus = OrderStatus.SHIPPED;
    }
}
