package com.saysimple.orders.services;

import com.saysimple.orders.dto.OrderDto;
import com.saysimple.orders.jpa.OrderEntity;

import java.util.List;

public interface OrderService {
    OrderDto create(OrderDto orderDto);
    Iterable<OrderEntity> list();
    OrderDto get(String orderId);
    Iterable<OrderEntity> listByUserId(String userId);
}
