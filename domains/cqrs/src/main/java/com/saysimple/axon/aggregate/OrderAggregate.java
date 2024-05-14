package com.saysimple.axon.aggregate;

import com.saysimple.axon.exceptions.OrderIsNotConfirmedException;
import com.saysimple.axon.model.command.ConfirmOrderCommand;
import com.saysimple.axon.model.command.CreateOrderCommand;
import com.saysimple.axon.model.command.ShipOrderCommand;
import com.saysimple.axon.model.event.OrderConfirmedEvent;
import com.saysimple.axon.model.event.OrderCreatedEvent;
import com.saysimple.axon.model.event.OrderShippedEvent;
import com.saysimple.axon.model.event.ProductQtyUpdatedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Aggregate(snapshotTriggerDefinition = "orderAggregateSnapshotTriggerDefinition")
public class OrderAggregate {
    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private OrderStatus orderStatus;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        apply(new OrderCreatedEvent(command));
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command) {
        apply(new OrderConfirmedEvent(command.getOrderId()));
    }

    @CommandHandler
    public void handle(ShipOrderCommand command) {
        if (orderStatus != OrderStatus.CONFIRMED) {
            throw new OrderIsNotConfirmedException(command.getOrderId(), command.getStatus());
        }

        apply(new OrderShippedEvent(command.getOrderId()));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.productId = event.getProductId();
        this.userId = event.getUserId();
        this.qty = event.getQty();
        this.unitPrice = event.getUnitPrice();
        this.totalPrice = event.getTotalPrice();
        this.orderStatus = OrderStatus.CREATED;
    }

    @EventSourcingHandler
    public void on(ProductQtyUpdatedEvent event) {
        this.qty = event.getQty();
        this.totalPrice = this.qty * this.unitPrice;
    }
}
