package com.saysimple.supports.entity;

import com.saysimple.supports.enums.ContactEnumType;
import com.saysimple.supports.enums.ProductEnumType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@DynamicInsert
@Setter
@Getter
@Table(name = "support")
public class Support implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String supportId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String categoryId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private ContactEnumType contactChoice;

    @Column(nullable = false)
    private String returnChoice;

    @Column(nullable = false)
    private ProductEnumType productImages;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
}