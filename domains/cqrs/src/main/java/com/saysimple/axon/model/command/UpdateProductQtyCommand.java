package com.saysimple.axon.model.command;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UpdateProductQtyCommand {
    private final String orderId;
    private final Integer qty;

    public UpdateProductQtyCommand(String orderId, Integer qty) {
        this.orderId = orderId;
        this.qty = qty;
    }
}
