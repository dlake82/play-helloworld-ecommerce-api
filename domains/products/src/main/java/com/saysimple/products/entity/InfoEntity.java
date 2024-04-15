package com.saysimple.products.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "\"info\"")
public class InfoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String productId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, name = "\"value\"")
    private String value;
}
