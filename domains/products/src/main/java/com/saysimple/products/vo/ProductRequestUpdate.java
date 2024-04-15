package com.saysimple.products.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductRequestUpdate extends ProductRequest {
    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "Name must be less then 255 characters")
    private String productId;
}
