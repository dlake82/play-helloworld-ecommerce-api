package com.saysimple.users.dto;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private String createdAt;

    private String encryptedPwd;
}
