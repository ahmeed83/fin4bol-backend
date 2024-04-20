package com.fin4bol.fin4bolbackend.controller.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ProductJson {
    private String name;
    private String eanNumber; // EAN-nummer
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String toUpdateEanNumber; // EAN-nummer that in case of update is used to find the product
    private Double purchaseCost; // Inkoopkosten
}