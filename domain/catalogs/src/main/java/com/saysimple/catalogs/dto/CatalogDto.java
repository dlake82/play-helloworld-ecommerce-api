package com.saysimple.catalogs.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CatalogDto implements Serializable {
    private Long id;
    private String productId;
    private String productName;
    private Integer unitPrice;
    private Integer stock;
    private Date createdAt;
}
