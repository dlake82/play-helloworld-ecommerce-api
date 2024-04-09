package com.saysimple.orders.controllers;

import com.saysimple.orders.dto.OrderDto;
import com.saysimple.orders.jpa.OrderEntity;
import com.saysimple.orders.services.OrderService;
import com.saysimple.orders.vo.RequestOrder;
import com.saysimple.orders.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    Environment env;
    OrderService orderService;

    DiscoveryClient discoveryClient;

    @Autowired
    public OrderController(Environment env, OrderService orderService, DiscoveryClient discoveryClient) {
        this.env = env;
        this.orderService = orderService;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/health-check")
    public String status() {
        List<ServiceInstance> serviceList = getApplications();
        for (ServiceInstance instance : serviceList) {
            System.out.println(String.format("instanceId:%s, serviceId:%s, host:%s, scheme:%s, uri:%s",
                    instance.getInstanceId(), instance.getServiceId(), instance.getHost(), instance.getScheme(), instance.getUri()));
        }

        return String.format("It's Working in Orders Service on LOCAL PORT %s (SERVER PORT %s)",
                env.getProperty("local.server.port"),
                env.getProperty("server.port"));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseOrder> create(@PathVariable("userId") String userId, @RequestBody RequestOrder order) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(order, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createdOrderDto = orderService.create(orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(createdOrderDto, ResponseOrder.class));
    }

    @GetMapping
    public ResponseEntity<List<ResponseOrder>> list() {
        Iterable<OrderEntity> orders = orderService.list();
        ModelMapper mapper = new ModelMapper();

        List<ResponseOrder> result = new ArrayList<>();
        orders.forEach(v -> {
            result.add(mapper.map(v, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseOrder> get(@PathVariable("orderId") String orderId) throws HttpException {
        log.info("OrderController get : " + orderId);
        OrderDto order = orderService.get(orderId);
        if (order == null) {
            throw new HttpException("Order not found");
        }

        ModelMapper mapper = new ModelMapper();

        return ResponseEntity.status(HttpStatus.OK).body(mapper.map(order, ResponseOrder.class));
    }

    @GetMapping("/{userId}/users")
    public ResponseEntity<List<ResponseOrder>> list(@PathVariable("userId") String userId) {
        Iterable<OrderEntity> orders = orderService.listByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orders.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    private List<ServiceInstance> getApplications() {

        List<String> services = this.discoveryClient.getServices();
        List<ServiceInstance> instances = new ArrayList<>();
        services.forEach(serviceName -> {
            this.discoveryClient.getInstances(serviceName).forEach(instance ->{
                instances.add(instance);
            });
        });
        return instances;
    }
}
