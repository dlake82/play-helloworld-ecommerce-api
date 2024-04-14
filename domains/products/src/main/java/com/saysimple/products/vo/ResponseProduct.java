package com.saysimple.products.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProduct {
    private String name;
    private Integer point;
    private Integer reviewCount;
    private Integer favoriteDecoCount;
    private Integer recentDecoCount;
    private Integer frequentDecoCount;
}
