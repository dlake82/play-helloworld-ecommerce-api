package com.saysimple.supports.dto;

import lombok.Data;

@Data
public class SupportDto {
    private String reviewId;
    private String productId;
    private String categoryId;
    private String userId;
    private String title;
    private String content;
    private Integer contactChoice;
    private Integer returnChoice;
    private Integer productImages;
}
