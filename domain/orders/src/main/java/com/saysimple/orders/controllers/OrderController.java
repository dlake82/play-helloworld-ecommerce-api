package com.saysimple.orders.controllers;

import com.saysimple.orders.dto.OrderDto;
import com.saysimple.orders.jpa.OrderEntity;
import com.saysimple.orders.messagequeue.KafkaProducer;
import com.saysimple.orders.messagequeue.OrderProducer;
import com.saysimple.orders.services.OrderService;
import com.saysimple.orders.vo.RequestOrder;
import com.saysimple.orders.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    Environment env;
    OrderService orderService;
    KafkaProducer kafkaProducer;
    DiscoveryClient discoveryClient;
    OrderProducer orderProducer;

    @Autowired
    public OrderController(Environment env, OrderService orderService, KafkaProducer kafkaProducer, DiscoveryClient discoveryClient, OrderProducer orderProducer) {
        this.env = env;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
        this.discoveryClient = discoveryClient;
        this.orderProducer = orderProducer;
    }

    @GetMapping("/health-check")
    public String status() {
        List<ServiceInstance> serviceList = getApplications();
        for (ServiceInstance instance : serviceList) {
            System.out.printf("instanceId:%s, serviceId:%s, host:%s, scheme:%s, uri:%s%n",
                    instance.getInstanceId(), instance.getServiceId(), instance.getHost(), instance.getScheme(), instance.getUri());
        }

        return String.format("It's Working in Orders Service on LOCAL PORT %s (SERVER PORT %s)",
                env.getProperty("local.server.port"),
                env.getProperty("server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> create(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);

        orderDto.setUserId(userId);
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

        kafkaProducer.send("example-catalog-topic", orderDto);
        orderProducer.send("order", orderDto);

        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
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
            this.discoveryClient.getInstances(serviceName).forEach(instance -> {
                instances.add(instance);
            });
        });
        return instances;
    }
}
