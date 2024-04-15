package com.saysimple.products.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDetails extends ProductResponse {
    private Integer money;
    private Integer point;
    private Integer reviewCount;
    private Integer favoriteDecoCount;
    private Integer recentDecoCount;
    private Integer frequentDecoCount;
}
