package com.saysimple.products.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProductDto {
    private String productId;
    private String name;
    private String category;
    private List<InfoDto> infos;
    private List<OptionDto> options;
}
