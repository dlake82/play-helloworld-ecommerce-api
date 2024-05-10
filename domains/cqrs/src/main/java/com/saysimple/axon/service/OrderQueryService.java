package com.saysimple.axon.service;

import com.saysimple.axon.aggregate.OrderAggregate;
import com.saysimple.axon.model.query.FindAllOrderedProductsQuery;
import com.saysimple.axon.model.query.OrderUpdatesQuery;
import com.saysimple.axon.model.query.TotalProductsShippedQuery;
import com.saysimple.axon.vo.OrderResponse;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderQueryService {

    private final QueryGateway queryGateway;

    public OrderQueryService(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    public CompletableFuture<List<OrderResponse>> findAllOrders() {
        return queryGateway.query(new FindAllOrderedProductsQuery(), ResponseTypes.multipleInstancesOf(OrderAggregate.class))
                .thenApply(r -> r.stream()
                        .map(OrderResponse::new)
                        .collect(Collectors.toList()));
    }

    public Flux<OrderResponse> allOrdersStreaming() {
        Publisher<OrderAggregate> publisher = queryGateway.streamingQuery(new FindAllOrderedProductsQuery(), OrderAggregate.class);
        return Flux.from(publisher)
                .map(OrderResponse::new);
    }

    public Integer totalShipped(String productId) {
        return queryGateway.scatterGather(new TotalProductsShippedQuery(productId), ResponseTypes.instanceOf(Integer.class), 10L, TimeUnit.SECONDS)
                .reduce(0, Integer::sum);
    }

    public Flux<OrderResponse> orderUpdates(String orderId) {
        return subscriptionQuery(new OrderUpdatesQuery(orderId), ResponseTypes.instanceOf(OrderAggregate.class)).map(OrderResponse::new);
    }

    private <Q, R> Flux<R> subscriptionQuery(Q query, ResponseType<R> resultType) {
        SubscriptionQueryResult<R, R> result = queryGateway.subscriptionQuery(query, resultType, resultType);
        return result.initialResult()
                .concatWith(result.updates())
                .doFinally(signal -> result.close());
    }
}
