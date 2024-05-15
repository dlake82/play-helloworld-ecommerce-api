package com.saysimple.axon.controller;

import com.saysimple.axon.model.command.ConfirmOrderCommand;
import com.saysimple.axon.model.command.CreateOrderCommand;
import com.saysimple.axon.model.command.ShipOrderCommand;
import com.saysimple.axon.model.command.UpdateProductQtyCommand;
import com.saysimple.axon.service.OrderQueryService;
import com.saysimple.axon.vo.CreateOrderRequest;
import com.saysimple.axon.vo.OrderResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
    public CompletableFuture<String> createOrder(
            @RequestBody CreateOrderRequest req
    ) {
        return commandGateway.send(new CreateOrderCommand(UUID.randomUUID().toString(), req.getProductId(), req.getUserId(), 1, 1000));
    }

    @PutMapping("/order/{order-id}/{qty}")
    public CompletableFuture<Void> updateQty(
            @PathVariable("order-id") String orderId,
            @PathVariable("qty") Integer qty) {
        return commandGateway.send(new UpdateProductQtyCommand(orderId, qty));
    }

    @PutMapping("/order/{order-id}/confirm")
    public CompletableFuture<Void> confirmOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new ConfirmOrderCommand(orderId));
    }

    @PutMapping("/order/{order-id}/ship")
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
