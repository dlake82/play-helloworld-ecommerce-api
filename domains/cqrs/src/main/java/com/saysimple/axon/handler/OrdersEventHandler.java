package com.saysimple.axon.handler;

import com.saysimple.axon.model.event.OrderConfirmedEvent;
import com.saysimple.axon.model.event.OrderCreatedEvent;
import com.saysimple.axon.model.event.OrderShippedEvent;
import com.saysimple.axon.model.event.ProductAddedEvent;
import com.saysimple.axon.model.event.ProductCountDecrementedEvent;
import com.saysimple.axon.model.event.ProductCountIncrementedEvent;
import com.saysimple.axon.model.event.ProductRemovedEvent;
import com.saysimple.axon.model.query.FindAllOrderedProductsQuery;
import com.saysimple.axon.model.query.Order;
import com.saysimple.axon.model.query.OrderUpdatesQuery;
import com.saysimple.axon.model.query.TotalProductsShippedQuery;

import org.reactivestreams.Publisher;

import java.util.List;

public interface OrdersEventHandler {

    void on(OrderCreatedEvent event);

    void on(ProductAddedEvent event);

    void on(ProductCountIncrementedEvent event);

    void on(ProductCountDecrementedEvent event);

    void on(ProductRemovedEvent event);

    void on(OrderConfirmedEvent event);

    void on(OrderShippedEvent event);

    List<Order> handle(FindAllOrderedProductsQuery query);

    Publisher<Order> handleStreaming(FindAllOrderedProductsQuery query);

    Integer handle(TotalProductsShippedQuery query);

    Order handle(OrderUpdatesQuery query);

    void reset(List<Order> orderList);
}
