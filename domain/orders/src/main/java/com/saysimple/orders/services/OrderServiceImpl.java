package com.saysimple.orders.services;

import com.saysimple.orders.dto.OrderDto;
import com.saysimple.orders.jpa.OrderEntity;
import com.saysimple.orders.jpa.OrderRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository catalogRepository) {
        this.orderRepository = catalogRepository;
    }


    @Override
    public OrderDto create(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);

        orderRepository.save(orderEntity);

        return mapper.map(orderEntity, OrderDto.class);

    }

    @Override
    public Iterable<OrderEntity> list(){
        return orderRepository.findAll();
    }

    @Override
    public OrderDto get(String orderId) {
        ModelMapper mapper = new ModelMapper();
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);

        if (orderEntity == null) {
            return null;
        }

        return mapper.map(orderEntity, OrderDto.class);
    }

    @Override
    public Iterable<OrderEntity> listByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
