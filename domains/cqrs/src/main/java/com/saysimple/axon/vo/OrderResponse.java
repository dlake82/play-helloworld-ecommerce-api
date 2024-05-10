package com.saysimple.axon.vo;

import com.saysimple.axon.aggregate.OrderAggregate;

import java.util.Map;

import static com.saysimple.axon.vo.OrderStatusResponse.toResponse;

public class OrderResponse {

    private String orderId;
    private Map<String, Integer> products;
    private OrderStatusResponse orderStatus;

    public OrderResponse(OrderAggregate orderAggregate) {
        this.orderId = orderAggregate.getOrderId();
        this.products = orderAggregate.getProducts();
        this.orderStatus = toResponse(orderAggregate.getOrderStatus());
    }

    /**
     * Added for the integration test, since it's using Jackson for the response
     */
    OrderResponse() {
    }

    public String getOrderId() {
        return orderId;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public OrderStatusResponse getOrderStatus() {
        return orderStatus;
    }
}
