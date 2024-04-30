package com.saysimple.axon.querymodel;

import com.saysimple.axon.handler.InMemoryOrdersEventHandler;
import com.saysimple.axon.handler.OrdersEventHandler;

public class InMemoryOrdersEventHandlerUnitTest extends AbstractOrdersEventHandlerUnitTest {

    @Override
    protected OrdersEventHandler getHandler() {
        return new InMemoryOrdersEventHandler(emitter);
    }
}
