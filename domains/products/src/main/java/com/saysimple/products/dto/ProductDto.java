package com.saysimple.products.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProductDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String decryptedPwd;

    private String encryptedPwd;

}
