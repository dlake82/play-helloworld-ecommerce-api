package com.saysimple.axon.controller;

import com.saysimple.axon.model.command.AddProductCommand;
import com.saysimple.axon.model.command.ConfirmOrderCommand;
import com.saysimple.axon.model.command.CreateOrderCommand;
import com.saysimple.axon.model.command.ShipOrderCommand;
import com.saysimple.axon.service.OrderQueryService;
import com.saysimple.axon.vo.OrderResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class OrdersController {

    private final CommandGateway commandGateway;
    private final OrderQueryService orderQueryService;

    public OrdersController(CommandGateway commandGateway, OrderQueryService orderQueryService) {
        this.commandGateway = commandGateway;
        this.orderQueryService = orderQueryService;
    }

    @PostMapping("/order")
    public CompletableFuture<String> createOrder() {
        return commandGateway.send(new CreateOrderCommand((UUID.randomUUID().toString())));
    }

    @PostMapping("/order/{order-id}/product/{product-id}")
    public CompletableFuture<Void> addProduct(@PathVariable("order-id") String orderId, @PathVariable("product-id") String productId) {
        return commandGateway.send(new AddProductCommand(orderId, productId));
    }

    @PostMapping("/order/{order-id}/confirm")
    public CompletableFuture<Void> confirmOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new ConfirmOrderCommand(orderId));
    }

    @PostMapping("/order/{order-id}/ship")
    public CompletableFuture<Void> shipOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new ShipOrderCommand(orderId));
    }

    @GetMapping("/all-orders")
    public CompletableFuture<List<OrderResponse>> findAllOrders() {
        return orderQueryService.findAllOrders();
    }

    @GetMapping(path = "/all-orders-streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OrderResponse> allOrdersStreaming() {
        return orderQueryService.allOrdersStreaming();
    }
}
