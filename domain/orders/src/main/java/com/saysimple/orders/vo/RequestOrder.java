package com.saysimple.orders.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestOrder {
    private String productId;
    private String qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String userId;
    private String orderId;
}
