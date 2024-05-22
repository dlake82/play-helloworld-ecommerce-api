package com.saysimple.supports.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSupport {
    private Long id;
    private String title;
    private String content;
    private String productId;
    private String contactChoice;
    private String returnChoice;
    private String productImages;
}
