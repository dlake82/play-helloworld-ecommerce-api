package com.saysimple.axon.querymodel;

import com.saysimple.axon.aggregate.OrderAggregate;
import com.saysimple.axon.handler.OrdersEventHandler;
import com.saysimple.axon.model.command.CreateOrderCommand;
import com.saysimple.axon.model.command.UpdateProductQtyCommand;
import com.saysimple.axon.model.event.*;
import com.saysimple.axon.model.query.FindAllOrderedProductsQuery;
import com.saysimple.axon.model.query.OrderUpdatesQuery;
import com.saysimple.axon.model.query.TotalProductsShippedQuery;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public abstract class AbstractOrdersEventHandlerUnitTest {

    private static final String ORDER_ID_1 = UUID.randomUUID()
            .toString();
    private static final String ORDER_ID_2 = UUID.randomUUID()
            .toString();
    private static final String PRODUCT_ID_1 = UUID.randomUUID()
            .toString();
    private static final String PRODUCT_ID_2 = UUID.randomUUID()
            .toString();
    private static final String USER_ID_1 = UUID.randomUUID()
            .toString();
    private static final String USER_ID_2 = UUID.randomUUID()
            .toString();
    private static OrderAggregate orderAggregateOne;
    private static OrderAggregate orderAggregateTwo;
    QueryUpdateEmitter emitter = mock(QueryUpdateEmitter.class);
    private OrdersEventHandler handler;

    @BeforeAll
    static void createOrders() {
        orderAggregateOne = new OrderAggregate(new CreateOrderCommand(ORDER_ID_1, PRODUCT_ID_1, USER_ID_1, 1, 1000));
        orderAggregateTwo = new OrderAggregate(new CreateOrderCommand(ORDER_ID_2, PRODUCT_ID_2, USER_ID_2, 1, 1000));
        orderAggregateTwo.setConfirmed();
    }

    @BeforeEach
    void setUp() {
        handler = getHandler();
    }

    protected abstract OrdersEventHandler getHandler();

    @Test
    @DisplayName("두 개의 주문을 초기화 한 후 모든 주문을 찾는 쿼리를 수행하면 두 개의 주문이 반환된다.")
    void givenTwoOrdersPlacedOfWhichOneNotShipped_whenFindAllOrderedProductsQuery_thenCorrectOrdersAreReturned() {
        resetWithTwoOrders();
        List<OrderAggregate> result = handler.handle(new FindAllOrderedProductsQuery());

        assertNotNull(result);
        assertEquals(2, result.size());

        OrderAggregate order_Aggregate_1 = result.stream()
                .filter(o -> o.getOrderId()
                        .equals(ORDER_ID_1))
                .findFirst()
                .orElse(null);
        assertEquals(orderAggregateOne, order_Aggregate_1);

        OrderAggregate order_Aggregate_2 = result.stream()
                .filter(o -> o.getOrderId()
                        .equals(ORDER_ID_2))
                .findFirst()
                .orElse(null);
        assertEquals(orderAggregateTwo, order_Aggregate_2);
    }

    @Test
    @DisplayName("두 개의 주문을 초기화 한 후 모든 주문을 찾는 쿼리를 스트리밍으로 수행하면 두 개의 주문이 반환된다.")
    void givenTwoOrdersPlacedOfWhichOneNotShipped_whenFindAllOrderedProductsQueryStreaming_thenCorrectOrdersAreReturned() {
        resetWithTwoOrders();
        final Consumer<OrderAggregate> orderVerifier = order -> {
            if (order.getOrderId()
                    .equals(orderAggregateOne.getOrderId())) {
                assertEquals(orderAggregateOne, order);
            } else if (order.getOrderId()
                    .equals(orderAggregateTwo.getOrderId())) {
                assertEquals(orderAggregateTwo, order);
            } else {
                throw new RuntimeException("Would expect either order one or order two");
            }
        };

        StepVerifier.create(Flux.from(handler.handleStreaming(new FindAllOrderedProductsQuery())))
                .assertNext(orderVerifier)
                .assertNext(orderVerifier)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("주문이 없는 경우 총 배송된 제품 쿼리를 수행하면 0이 반환된다.")
    void givenNoOrdersPlaced_whenTotalProductsShippedQuery_thenZeroReturned() {
        assertEquals(0, handler.handle(new TotalProductsShippedQuery(PRODUCT_ID_1)));
    }

    @Test
    @DisplayName("주문 상태를 확인 상태로 수정하고 주문 조회 쿼리를 수행하면 주문의 상태가 확인 상태로 반환된다.")
    void givenOrderPlaced_whenOrderUpdatesQuery_thenOrderHasThreeProducts() {
        resetWithTwoOrders();

        orderAggregateOne.handle(new UpdateProductQtyCommand(ORDER_ID_1, 3));

        assertEquals(3, handler.handle(new OrderUpdatesQuery(ORDER_ID_1)));
    }

    @Test
    @DisplayName("두 개의 주문을 초기화 한 후 각 주문의 총 배송된 제품 쿼리를 수행하면 각 주문의 제품 수가 반환된다.")
    void givenTwoOrdersPlacedOfWhichOneNotShipped_whenTotalProductsShippedQuery_thenOnlyCountProductsFirstOrder() {
        resetWithTwoOrders();

        assertEquals(3, handler.handle(new TotalProductsShippedQuery(PRODUCT_ID_1)));
        assertEquals(0, handler.handle(new TotalProductsShippedQuery(PRODUCT_ID_2)));
    }

    @Test
    @DisplayName("두 개의 주문을 초기화 한 후 두 번째 주문에 대한 주문 배송 이벤트를 수행하면 두 번째 주문의 제품 수가 결과에 반영되어야 한다.")
    void givenTwoOrdersPlacedAndShipped_whenTotalProductsShippedQuery_thenCountBothOrders() {
        resetWithTwoOrders();
        handler.on(new OrderShippedEvent(ORDER_ID_2));

        assertEquals(4, handler.handle(new TotalProductsShippedQuery(PRODUCT_ID_1)));
        assertEquals(1, handler.handle(new TotalProductsShippedQuery(PRODUCT_ID_2)));
    }

    private void resetWithTwoOrders() {
        handler.reset(Arrays.asList(orderAggregateOne, orderAggregateTwo));
    }
}
