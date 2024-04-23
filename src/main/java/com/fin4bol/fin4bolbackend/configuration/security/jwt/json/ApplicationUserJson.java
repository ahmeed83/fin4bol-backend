package com.fin4bol.fin4bolbackend.configuration.security.jwt.json;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplicationUserJson {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Password confirmation is required")
    private String passwordConfirm;
    @NotBlank(message = "Name is required")
    private String name;
    private String referralSource;
}
