package com.saysimple.axon.querymodel;

import com.saysimple.axon.OrderApplication;
import com.saysimple.axon.aggregate.OrderAggregate;
import com.saysimple.axon.handler.OrdersEventHandler;
import com.saysimple.axon.model.command.CreateOrderCommand;
import com.saysimple.axon.model.event.*;
import com.saysimple.axon.service.OrderQueryService;
import com.saysimple.axon.vo.OrderResponse;
import com.saysimple.axon.vo.OrderStatusResponse;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        OrderAggregate orderAggregate = new OrderAggregate(new CreateOrderCommand(orderId, productId, userId, 1, 1000));
        handler.reset(Collections.singletonList(orderAggregate));
    }

    @Test
    void givenOrderCreatedEventSend_whenCallingAllOrders_thenOneCreatedOrderIsReturned() throws ExecutionException, InterruptedException {
        List<OrderResponse> result = queryService.findAllOrders()
                .get();
        assertEquals(1, result.size());
        OrderResponse response = result.get(0);
        assertEquals(orderId, response.getOrderId());
        assertEquals(productId, response.getProductId());
        assertEquals(OrderStatusResponse.CREATED, response.getOrderStatus());
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
        OrderAggregate orderAggregate = new OrderAggregate(new CreateOrderCommand(orderId, productId, userId, 1, 1000));
        orderAggregate.setShipped();
        handler.reset(Collections.singletonList(orderAggregate));

        assertEquals(237, queryService.totalShipped(productId));
    }

    @Test
    @DisplayName("주문 생성 및 상품  확인하고 배송하면 업데이트된 주문이 반환된다.")
    void givenOrdersAreUpdated_whenCallingOrderUpdates_thenUpdatesReturned() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(this::addIncrementDecrementConfirmAndShip, 100L, TimeUnit.MILLISECONDS);
        try {
            StepVerifier.create(queryService.orderUpdates(orderId))
                    .assertNext(order -> assertEquals(productId, order.getProductId()))
                    .assertNext(order -> assertEquals(1, order.getQty()))
                    .assertNext(order -> assertEquals(2, order.getQty()))
                    .assertNext(order -> assertEquals(1, order.getQty()))
                    .assertNext(order -> assertEquals(OrderStatusResponse.CONFIRMED, order.getOrderStatus()))
                    .assertNext(order -> assertEquals(OrderStatusResponse.SHIPPED, order.getOrderStatus()))
                    .thenCancel()
                    .verify();
        } finally {
            executor.shutdown();
        }
    }

    private void addIncrementDecrementConfirmAndShip() {
        sendProductCountIncrementEvent();
        sendProductCountDecrementEvent();
        sendOrderConfirmedEvent();
        sendOrderShippedEvent();
    }

    private void sendProductCountIncrementEvent() {
        eventGateway.publish(new ProductQtyUpdatedEvent(orderId, 2));
    }

    private void sendProductCountDecrementEvent() {
        eventGateway.publish(new ProductQtyUpdatedEvent(orderId, 1));
    }

    private void sendOrderConfirmedEvent() {
        eventGateway.publish(new OrderConfirmedEvent(orderId));
    }

    private void sendOrderShippedEvent() {
        eventGateway.publish(new OrderShippedEvent(orderId));
    }
}
