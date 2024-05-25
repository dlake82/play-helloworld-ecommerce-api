package com.saysimple.supports.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestSupport {
    @NotNull(message = "supportId cannot be null")
    private String supportId;

    @NotNull(message = "productId cannot be null")
    private String productId;

    @NotNull(message = "userId cannot be null")
    private String userId;
    private String categoryId;
    private String title;
    private String content;
    private String contactChoice;
    private String returnChoice;
    private String productImages;
}
