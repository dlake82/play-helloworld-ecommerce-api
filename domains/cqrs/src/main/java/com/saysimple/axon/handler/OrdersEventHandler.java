package com.saysimple.axon.handler;

import com.saysimple.axon.aggregate.OrderAggregate;
import com.saysimple.axon.model.event.OrderConfirmedEvent;
import com.saysimple.axon.model.event.OrderCreatedEvent;
import com.saysimple.axon.model.event.OrderShippedEvent;
import com.saysimple.axon.model.query.FindAllOrderedProductsQuery;
import com.saysimple.axon.model.query.OrderUpdatesQuery;
import com.saysimple.axon.model.query.TotalProductsShippedQuery;
import org.reactivestreams.Publisher;

import java.util.List;

public interface OrdersEventHandler {

    void on(OrderCreatedEvent event);

    void on(OrderConfirmedEvent event);

    void on(OrderShippedEvent event);

    List<OrderAggregate> handle(FindAllOrderedProductsQuery query);

    Publisher<OrderAggregate> handleStreaming(FindAllOrderedProductsQuery query);

    Integer handle(TotalProductsShippedQuery query);

    OrderAggregate handle(OrderUpdatesQuery query);

    void reset(List<OrderAggregate> orderAggregateList);
}
