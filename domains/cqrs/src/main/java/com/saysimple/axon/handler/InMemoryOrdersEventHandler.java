package com.saysimple.axon.handler;

import com.saysimple.axon.aggregate.OrderAggregate;
import com.saysimple.axon.aggregate.OrderStatus;
import com.saysimple.axon.model.event.OrderConfirmedEvent;
import com.saysimple.axon.model.event.OrderCreatedEvent;
import com.saysimple.axon.model.event.OrderShippedEvent;
import com.saysimple.axon.model.event.ProductQtyUpdatedEvent;
import com.saysimple.axon.model.query.FindAllOrderedProductsQuery;
import com.saysimple.axon.model.query.OrderUpdatesQuery;
import com.saysimple.axon.model.query.TotalProductsShippedQuery;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ProcessingGroup("orders")
@Profile("!mongo")
public class InMemoryOrdersEventHandler implements OrdersEventHandler {
    private final Map<String, OrderAggregate> orders = new HashMap<>();
    private final QueryUpdateEmitter emitter;

    public InMemoryOrdersEventHandler(QueryUpdateEmitter emitter) {
        this.emitter = emitter;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        orders.put(event.getOrderId(), new OrderAggregate(event));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        orders.computeIfPresent(event.getOrderId(), (orderId, order) -> {
            order.setConfirmed();
            emitUpdate(order);
            return order;
        });
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        orders.computeIfPresent(event.getOrderId(), (orderId, order) -> {
            order.setShipped();
            emitUpdate(order);
            return order;
        });
    }

    @EventHandler
    public void on(ProductQtyUpdatedEvent event) {
        orders.computeIfPresent(event.getOrderId(), (orderId, order) -> {
            order.setQty(event.getQty());
            emitUpdate(order);
            return order;
        });
    }

    @QueryHandler
    public List<OrderAggregate> handle(FindAllOrderedProductsQuery query) {
        return new ArrayList<>(orders.values());
    }

    @QueryHandler
    public Publisher<OrderAggregate> handleStreaming(FindAllOrderedProductsQuery query) {
        return Mono.fromCallable(orders::values)
                .flatMapMany(Flux::fromIterable);
    }

    @QueryHandler
    public Integer handle(TotalProductsShippedQuery query) {
        return (int) orders.values()
                .stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.SHIPPED).count();
    }

    @QueryHandler
    public OrderAggregate handle(OrderUpdatesQuery query) {
        return orders.get(query.orderId());
    }

    private void emitUpdate(OrderAggregate orderAggregate) {
        emitter.emit(OrderUpdatesQuery.class, q -> orderAggregate.getOrderId()
                .equals(q.orderId()), orderAggregate);
    }

    @Override
    public void reset(List<OrderAggregate> orderAggregateList) {
        orders.clear();
        orderAggregateList.forEach(o -> orders.put(o.getOrderId(), o));
    }
}
