package com.saysimple.axon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOrderRequest {
    private String productId;
    private String userId;
}
