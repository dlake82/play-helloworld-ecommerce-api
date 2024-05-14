package com.saysimple.axon.vo;

import com.saysimple.axon.aggregate.OrderAggregate;
import lombok.Data;

import java.util.Map;

import static com.saysimple.axon.vo.OrderStatusResponse.toResponse;

@Data
public class OrderResponse {

    private String orderId;
    private String productId;
    private String userId;
    private OrderStatusResponse orderStatus;

    public OrderResponse(OrderAggregate orderAggregate) {
        this.orderId = orderAggregate.getOrderId();
        this.productId = orderAggregate.getProductId();
        this.userId = orderAggregate.getUserId();
        this.orderStatus = toResponse(orderAggregate.getOrderStatus());
    }

    /**
     * Added for the integration test, since it's using Jackson for the response
     */
    OrderResponse() {
    }
}
