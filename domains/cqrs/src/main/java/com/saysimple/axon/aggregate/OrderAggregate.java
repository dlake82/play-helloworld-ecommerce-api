package com.saysimple.axon.aggregate;

import com.saysimple.axon.exceptions.DuplicateOrderLineException;
import com.saysimple.axon.exceptions.OrderAlreadyConfirmedException;
import com.saysimple.axon.model.command.CreateOrderCommand;
import com.saysimple.axon.model.command.ShipOrderCommand;
import com.saysimple.axon.model.event.OrderCreatedEvent;
import com.saysimple.axon.model.event.ProductAddedEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Data
@Aggregate(snapshotTriggerDefinition = "orderAggregateSnapshotTriggerDefinition")
public class OrderAggregate {
    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private OrderStatus orderStatus;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        apply(new OrderCreatedEvent(command.getOrderId(), command.getProductId(), command.getUserId()));
    }

    protected OrderAggregate() {
        // Required by Axon to build a default Aggregate prior to Event Sourcing
    }

    @CommandHandler
    public void handle(ShipOrderCommand command) {
        if (orderStatus == OrderStatus.SHIPPED) {
            throw new OrderAlreadyConfirmedException(orderId);
        }

        String productId = command.getProductId();
        if (orderLines.containsKey(productId)) {
            throw new DuplicateOrderLineException(productId);
        }
        apply(new ProductAddedEvent(orderId, productId));
    }

    @CommandHandler
    public void handle() {
        if (orderConfirmed) {
            throw new OrderAlreadyConfirmedException(orderId);
        }

        String productId = command.getProductId();
        if (orderLines.containsKey(productId)) {
            throw new DuplicateOrderLineException(productId);
        }
        apply(new ProductAddedEvent(orderId, productId));
    }

    @CommandHandler
    public void handle() {
        if (orderConfirmed) {
            throw new OrderAlreadyConfirmedException(orderId);
        }

        String productId = command.getProductId();
        if (orderLines.containsKey(productId)) {
            throw new DuplicateOrderLineException(productId);
        }
        apply(new ProductAddedEvent(orderId, productId));
    }
}
