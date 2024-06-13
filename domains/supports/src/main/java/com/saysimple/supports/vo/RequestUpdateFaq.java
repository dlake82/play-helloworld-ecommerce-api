package com.saysimple.supports.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RequestUpdateFaq extends RequestFaq{
    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "Name must be less then 255 characters")
    private String faqId;
}
