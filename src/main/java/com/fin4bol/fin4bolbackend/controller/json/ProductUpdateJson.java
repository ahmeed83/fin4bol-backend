package com.fin4bol.fin4bolbackend.controller.json;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProductUpdateJson {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "eanNumber is mandatory")
    @Pattern(regexp = "^[0-9]{1,20}$", message = "EAN number must be less than 20 digits")
    private String eanNumber; // EAN-nummer
    @NotBlank(message = "toUpdateEanNumber is mandatory")
    @Pattern(regexp = "^[0-9]{1,20}$", message = "EAN number must be less than 20 digits")
    private String toUpdateEanNumber; // EAN-nummer that in case of update is used to find the product
    @DecimalMin(value = "1.0", message = "Purchase cost must be more than 1")
    private Double purchaseCost; // Inkoopkosten
}