package com.saysimple.axon.uow;

import com.saysimple.axon.exceptions.UnconfirmedOrderException;
import com.saysimple.axon.model.command.ConfirmOrderCommand;
import com.saysimple.axon.model.command.CreateOrderCommand;
import com.saysimple.axon.model.command.ShipOrderCommand;
import com.saysimple.axon.model.event.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashMap;
import java.util.Map;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate(snapshotTriggerDefinition = "orderAggregateSnapshotTriggerDefinition")
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private boolean orderConfirmed;

    @AggregateMember
    private Map<String, OrderLine> orderLines;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        apply(new OrderCreatedEvent(command));
    }

    protected OrderAggregate() {
        // Required by Axon to build a default Aggregate prior to Event Sourcing
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command) {
        if (orderConfirmed) {
            return;
        }

        apply(new OrderConfirmedEvent(orderId));
    }

    @CommandHandler
    public void handle(ShipOrderCommand command) {
        if (!orderConfirmed) {
            throw new UnconfirmedOrderException();
        }

        apply(new OrderShippedEvent(orderId));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.orderConfirmed = false;
        this.orderLines = new HashMap<>();
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        this.orderConfirmed = true;
    }

    @EventSourcingHandler
    public void on(ProductAddedEvent event) {
        String productId = event.getProductId();
        this.orderLines.put(productId, new OrderLine(productId));
    }

    @EventSourcingHandler
    public void on(ProductRemovedEvent event) {
        this.orderLines.remove(event.getProductId());
    }
}
