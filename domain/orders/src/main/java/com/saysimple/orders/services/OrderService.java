package com.saysimple.orders.services;

import com.saysimple.orders.dto.OrderDto;
import com.saysimple.orders.jpa.OrderEntity;

public interface OrderService {
    OrderDto create(OrderDto orderDto);
    OrderDto get(String orderId);
    Iterable<OrderEntity> listByUserId(String userId);
}
