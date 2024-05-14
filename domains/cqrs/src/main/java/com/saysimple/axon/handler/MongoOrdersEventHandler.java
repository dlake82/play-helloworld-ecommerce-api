package com.saysimple.axon.handler;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.UpdateResult;
import com.saysimple.axon.aggregate.OrderAggregate;
import com.saysimple.axon.aggregate.OrderStatus;
import com.saysimple.axon.model.command.CreateOrderCommand;
import com.saysimple.axon.model.event.OrderConfirmedEvent;
import com.saysimple.axon.model.event.OrderCreatedEvent;
import com.saysimple.axon.model.event.OrderShippedEvent;
import com.saysimple.axon.model.query.FindAllOrderedProductsQuery;
import com.saysimple.axon.model.query.OrderUpdatesQuery;
import com.saysimple.axon.model.query.TotalProductsShippedQuery;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;

@Service
@ProcessingGroup("orders")
@Profile("mongo")
public class MongoOrdersEventHandler implements OrdersEventHandler {

    static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup()
            .lookupClass());
    private static final String ORDER_COLLECTION_NAME = "orders";
    private static final String AXON_FRAMEWORK_DATABASE_NAME = "axonframework";
    private static final String ORDER_ID_PROPERTY_NAME = "orderId";
    private static final String PRODUCTS_PROPERTY_NAME = "productId";
    private static final String USERS_PROPERTY_NAME = "userId";
    private static final String ORDER_STATUS_PROPERTY_NAME = "orderStatus";
    private final MongoCollection<Document> orders;
    private final QueryUpdateEmitter emitter;

    public MongoOrdersEventHandler(MongoClient client, QueryUpdateEmitter emitter) {
        orders = client.getDatabase(AXON_FRAMEWORK_DATABASE_NAME)
                .getCollection(ORDER_COLLECTION_NAME);
        orders.createIndex(Indexes.ascending(ORDER_ID_PROPERTY_NAME), new IndexOptions().unique(true));
        this.emitter = emitter;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        orders.insertOne(orderToDocument(new OrderAggregate(
                new CreateOrderCommand(event.getOrderId(), event.getProductId(), event.getUserId()))
        ));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        update(event.getOrderId(), OrderAggregate::setOrderConfirmed);
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        update(event.getOrderId(), OrderAggregate::setOrderShipped);
    }

    @QueryHandler
    public List<OrderAggregate> handle(FindAllOrderedProductsQuery query) {
        List<OrderAggregate> orderAggregateList = new ArrayList<>();
        orders.find()
                .forEach(d -> orderAggregateList.add(documentToOrder(d)));
        return orderAggregateList;
    }

    @Override
    public Publisher<OrderAggregate> handleStreaming(FindAllOrderedProductsQuery query) {
        return Flux.fromIterable(orders.find())
                .map(this::documentToOrder);
    }

    @QueryHandler
    public Integer handle(TotalProductsShippedQuery query) {
        AtomicInteger result = new AtomicInteger();
        orders.find(shippedProductFilter(query.productId()))
                .map(d -> d.get(PRODUCTS_PROPERTY_NAME, Document.class))
                .map(d -> d.getInteger(query.productId(), 0))
                .forEach(result::addAndGet);
        return result.get();
    }

    @QueryHandler
    public OrderAggregate handle(OrderUpdatesQuery query) {
        return getOrder(query.orderId()).orElse(null);
    }

    @Override
    public void reset(List<OrderAggregate> orderAggregateList) {
        orders.deleteMany(new Document());
        orderAggregateList.forEach(o -> orders.insertOne(orderToDocument(o)));
    }

    private Optional<OrderAggregate> getOrder(String orderId) {
        return Optional.ofNullable(orders.find(eq(ORDER_ID_PROPERTY_NAME, orderId))
                        .first())
                .map(this::documentToOrder);
    }

    private OrderAggregate emitUpdate(OrderAggregate orderAggregate) {
        emitter.emit(OrderUpdatesQuery.class, q -> orderAggregate.getOrderId()
                .equals(q.orderId()), orderAggregate);
        return orderAggregate;
    }

    private OrderAggregate updateOrder(OrderAggregate orderAggregate, Consumer<OrderAggregate> updateFunction) {
        updateFunction.accept(orderAggregate);
        return orderAggregate;
    }

    private UpdateResult persistUpdate(OrderAggregate orderAggregate) {
        return orders.replaceOne(eq(ORDER_ID_PROPERTY_NAME, orderAggregate.getOrderId()), orderToDocument(orderAggregate));
    }

    private void update(String orderId, Consumer<OrderAggregate> updateFunction) {
        UpdateResult result = getOrder(orderId).map(o -> updateOrder(o, updateFunction))
                .map(this::emitUpdate)
                .map(this::persistUpdate)
                .orElse(null);
        logger.info("Result of updating order with orderId '{}': {}", orderId, result);
    }

    private Document orderToDocument(OrderAggregate orderAggregate) {
        return new Document(ORDER_ID_PROPERTY_NAME, orderAggregate.getOrderId())
                .append(PRODUCTS_PROPERTY_NAME, orderAggregate.getProductId())
                .append(USERS_PROPERTY_NAME, orderAggregate.getUserId());
    }

    private OrderAggregate documentToOrder(@NonNull Document document) {
        OrderAggregate orderAggregate = new OrderAggregate(document.getString(ORDER_ID_PROPERTY_NAME), document.getString(PRODUCTS_PROPERTY_NAME), document.getString(USERS_PROPERTY_NAME));
        String status = document.getString(ORDER_STATUS_PROPERTY_NAME);
        if (OrderStatus.CONFIRMED.toString()
                .equals(status)) {
            orderAggregate.setOrderConfirmed();
        } else if (OrderStatus.SHIPPED.toString()
                .equals(status)) {
            orderAggregate.setOrderShipped();
        }
        return orderAggregate;
    }

    private Bson shippedProductFilter(String productId) {
        return and(eq(ORDER_STATUS_PROPERTY_NAME, OrderStatus.SHIPPED.toString()), exists(String.format(PRODUCTS_PROPERTY_NAME + ".%s", productId)));
    }
}
