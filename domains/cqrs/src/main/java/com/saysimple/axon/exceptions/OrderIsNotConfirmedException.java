package com.saysimple.axon.exceptions;

import com.saysimple.axon.aggregate.OrderStatus;

public class OrderIsNotConfirmedException extends IllegalStateException {

    public OrderIsNotConfirmedException(String orderId) {
        super("Order [" + orderId + "] status isn't confirmed.");
    }
}
