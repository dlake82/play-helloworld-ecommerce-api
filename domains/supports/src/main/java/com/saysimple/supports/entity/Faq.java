package com.saysimple.supports.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@DynamicInsert
@Table(name="faq")
public class Faq implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String faqId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
