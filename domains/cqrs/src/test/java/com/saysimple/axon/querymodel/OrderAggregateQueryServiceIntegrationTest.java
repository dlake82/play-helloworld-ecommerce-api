package com.saysimple.axon.querymodel;

import com.saysimple.axon.OrderApplication;
import com.saysimple.axon.aggregate.OrderAggregate;
import com.saysimple.axon.handler.OrdersEventHandler;
import com.saysimple.axon.model.event.*;
import com.saysimple.axon.service.OrderQueryService;
import com.saysimple.axon.vo.OrderResponse;
import com.saysimple.axon.vo.OrderStatusResponse;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = OrderApplication.class)
class OrderAggregateQueryServiceIntegrationTest {

    private final String productId = "Deluxe Chair";
    @Autowired
    OrderQueryService queryService;
    @Autowired
    EventGateway eventGateway;
    @Autowired
    OrdersEventHandler handler;
    private String orderId;
    private String userId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID()
                .toString();
        userId = UUID.randomUUID()
                .toString();
        OrderAggregate orderAggregate = new OrderAggregate(orderId, productId, userId);
        handler.reset(Collections.singletonList(orderAggregate));
    }

    @Test
    void givenOrderCreatedEventSend_whenCallingAllOrders_thenOneCreatedOrderIsReturned() throws ExecutionException, InterruptedException {
        List<OrderResponse> result = queryService.findAllOrders()
                .get();
        assertEquals(1, result.size());
        OrderResponse response = result.get(0);
        assertEquals(orderId, response.getOrderId());
        assertEquals(OrderStatusResponse.CREATED, response.getOrderStatus());
        assertTrue(response.getProducts()
                .isEmpty());
    }

    @Test
    void givenOrderCreatedEventSend_whenCallingAllOrdersStreaming_thenOneOrderIsReturned() {
        Flux<OrderResponse> result = queryService.allOrdersStreaming();
        StepVerifier.create(result)
                .assertNext(order -> assertEquals(orderId, order.getOrderId()))
                .expectComplete()
                .verify();
    }

    @Test
    void givenThreeDeluxeChairsShipped_whenCallingAllShippedChairs_then234PlusTreeIsReturned() {
        OrderAggregate orderAggregate = new OrderAggregate(orderId, productId, userId);
        orderAggregate.setOrderShipped();
        handler.reset(Collections.singletonList(orderAggregate));

        assertEquals(237, queryService.totalShipped(productId));
    }

    @Test
    void givenOrdersAreUpdated_whenCallingOrderUpdates_thenUpdatesReturned() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(this::addIncrementDecrementConfirmAndShip, 100L, TimeUnit.MILLISECONDS);
        try {
            StepVerifier.create(queryService.orderUpdates(orderId))
                    .assertNext(order -> assertTrue(order.getProducts()
                            .isEmpty()))
                    .assertNext(order -> assertEquals(1, order.getProducts()
                            .get(productId)))
                    .assertNext(order -> assertEquals(2, order.getProducts()
                            .get(productId)))
                    .assertNext(order -> assertEquals(1, order.getProducts()
                            .get(productId)))
                    .assertNext(order -> assertEquals(OrderStatusResponse.CONFIRMED, order.getOrderStatus()))
                    .assertNext(order -> assertEquals(OrderStatusResponse.SHIPPED, order.getOrderStatus()))
                    .thenCancel()
                    .verify();
        } finally {
            executor.shutdown();
        }
    }

    private void addIncrementDecrementConfirmAndShip() {
        sendProductAddedEvent();
        sendProductCountIncrementEvent();
        sendProductCountDecrementEvent();
        sendOrderConfirmedEvent();
        sendOrderShippedEvent();
    }

    private void sendProductAddedEvent() {
        ProductAddedEvent event = new ProductAddedEvent(orderId, productId);
        eventGateway.publish(event);
    }

    private void sendProductCountIncrementEvent() {
        ProductCountIncrementedEvent event = new ProductCountIncrementedEvent(orderId, productId);
        eventGateway.publish(event);
    }

    private void sendProductCountDecrementEvent() {
        ProductCountDecrementedEvent event = new ProductCountDecrementedEvent(orderId, productId);
        eventGateway.publish(event);
    }

    private void sendOrderConfirmedEvent() {
        OrderConfirmedEvent event = new OrderConfirmedEvent(orderId);
        eventGateway.publish(event);
    }

    private void sendOrderShippedEvent() {
        OrderShippedEvent event = new OrderShippedEvent(orderId);
        eventGateway.publish(event);
    }
}