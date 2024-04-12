package com.saysimple.catalogs.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestCatalog {
    private String productId;
    private String productName;
    private Integer unitPrice;
    private Integer stock;
}
