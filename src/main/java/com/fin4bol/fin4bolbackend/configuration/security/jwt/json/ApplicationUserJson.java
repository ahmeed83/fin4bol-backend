package com.fin4bol.fin4bolbackend.configuration.security.jwt.json;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplicationUserJson {

    @NotBlank(message = "Username is required")
    private String userName;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Password does not match")
    private String passwordConfirm;
    @NotBlank(message = "Email is required")
    private String email;
    private String referralSource;
}
