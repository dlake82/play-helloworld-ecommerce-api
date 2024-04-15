package com.saysimple.products.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class InfoDto {
    private String name;
    private String value;
}
