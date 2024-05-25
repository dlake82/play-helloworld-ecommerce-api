package com.saysimple.supports.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class SupportSearchDto {
    private Long id;
    private String supportId;
    private String title;

    private String type;
    private String keyword;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
