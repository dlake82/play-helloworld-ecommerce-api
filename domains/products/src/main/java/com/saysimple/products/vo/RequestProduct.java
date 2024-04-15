package com.saysimple.products.vo;

import com.saysimple.products.dto.InfoDto;
import com.saysimple.products.dto.OptionDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RequestProduct {
    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "productId must be less then 255 characters")
    private String name;
    private String category;
    private List<InfoDto> infos;
    private List<OptionDto> options;
}
