package com.saysimple.axon.exceptions;

import com.saysimple.axon.aggregate.OrderStatus;

public class OrderIsNotConfirmedException extends IllegalStateException {

    public OrderIsNotConfirmedException(String orderId, OrderStatus status) {
        super("Order [" + orderId + "] status [" + status + "] isn't confirmed.");
    }
}
