package com.fin4bol.fin4bolbackend.controller.json;

import lombok.Data;

@Data
public class PerformanceJson {
    private String name; // Product Naam
    private String eanNumber; // EAN-nummer
    private Double purchaseCost; // Inkoopkosten
    private Double averagePricePerProduct; // Gemiddelde Prijs per Product
    private Double revenue; // Omzet
    private Integer salesVolume; // Afzet
    private Double vat; // BTW
    private Double commission; // Commissie
    private Double commissionCorrection; // Correctie Commissie
    private Double lostItemCompensation; // Compensatie Zoekgeraakte Artikelen
    private Double netCommission; // Netto Commissie
    private Double shippingCost; // Verzendkosten
    private Double shippingCostCorrection; // Correctie Verzendkosten
    private Double bolComShippingLabelCost; // VVB Verzendzegel
    private Double totalShippingCost; // Totaal Verzendkosten
    private Double unsellableInventoryCost; // Onverkoopbare Voorraadkosten
    private Double pickPackCost; // Pick&Pack Kosten
    private Double pickPackCostCorrection; // Correctie Pick&Pack Kosten
    private Double netPickPackCost; // Netto Pick&Pack Kosten
    private Double inventoryCost; // Voorraadkosten
    private Double salesPriceCorrection; // Correctie Verkoopprijs Artikelen
    private Double salesPriceCorrectionVat; // Correctie Verkoopprijs Artikelen BTW
}

