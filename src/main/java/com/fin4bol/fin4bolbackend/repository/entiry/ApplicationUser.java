package com.fin4bol.fin4bolbackend.repository.entiry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


/**
 * Application User class. This class will store the user data in the database.
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "APPLICATION_USER")
public class ApplicationUser extends BaseModel {

    @NotBlank(message = "Username is required")
    @Column(unique = true)
    private String userName;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Password does not match")
    @Transient
    private String passwordConfirm;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank
    private String role;
    @NotNull
    private boolean isEnabled;
    private String provider;
    private String referralSource;
}