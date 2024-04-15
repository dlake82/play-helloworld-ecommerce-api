package com.saysimple.products.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.saysimple.products.dto.InfoDto;
import com.saysimple.products.dto.OptionDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProduct {
    private String productId;
    private String name;
    private List<InfoDto> infos;
    private List<OptionDto> options;
}
