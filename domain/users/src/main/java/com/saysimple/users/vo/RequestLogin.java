package com.saysimple.users.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLogin {
    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email must cannot be less than two characters")
    private String email;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must cannot be less than eight characters")
    private String password;
}
