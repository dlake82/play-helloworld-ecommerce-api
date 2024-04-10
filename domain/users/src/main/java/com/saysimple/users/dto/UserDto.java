package com.saysimple.users.dto;

import com.saysimple.users.vo.ResponseOrder;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private String createdAt;

    private String encryptedPwd;

    private List<ResponseOrder> orders;
}
