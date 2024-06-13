package com.saysimple.supports.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFaq {
    @NotNull(message = "faqId cannot be null")
    private String faqId;

    @NotNull(message = "userId cannot be null")
    private String userId;

    private String title;
    private String content;
}
