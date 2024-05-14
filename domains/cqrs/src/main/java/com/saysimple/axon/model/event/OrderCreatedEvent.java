package com.saysimple.axon.model.event;

import com.saysimple.axon.model.command.CreateOrderCommand;
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
    private final Integer qty;
    private final Integer unitPrice;
    private final Integer totalPrice;

    public OrderCreatedEvent(CreateOrderCommand command) {
        this.orderId = command.getOrderId();
        this.productId = command.getProductId();
        this.userId = command.getUserId();
        this.qty = command.getQty();
        this.unitPrice = command.getUnitPrice();
        this.totalPrice = this.qty * this.unitPrice;
    }
}
