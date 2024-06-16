package com.saysimple.supports.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestFaq {
    @NotNull(message = "faqId cannot be null")
    private String faqId;

    @NotNull(message = "userId cannot be null")
    private String userId;

    private String title;
    private String content;
}
