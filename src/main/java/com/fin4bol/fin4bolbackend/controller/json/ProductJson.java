package com.fin4bol.fin4bolbackend.controller.json;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProductJson {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "EAN number is mandatory")
    @Pattern(regexp = "^\\d{1,20}$", message = "Only digits are allowed and EAN number must be less than 20 digits")
    private String eanNumber; // EAN-nummer
    @NotNull(message = "Purchase cost is mandatory")
    @DecimalMin(value = "1.0", message = "Purchase cost must be more than 1")
    private Double purchaseCost; // Inkoopkosten
}