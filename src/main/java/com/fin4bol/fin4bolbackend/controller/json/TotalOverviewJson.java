package com.fin4bol.fin4bolbackend.controller.json;

import lombok.Data;

@Data
public class TotalOverviewJson {
    /**
     * De som van Sales Volume
     */
    private Integer sales;
    /**
     * De som van Revenue
     */
    private Double revenue;
    /**
     * De som van product formule, i.e. vermenigvuldig voor elk product de inkoopprijs met het aantal sales en tel dat bij elkaar op voor de totale inkoopkosten
     */
    private Double purchaseCost;
    /**
     * De som van Total shipping cost
     */
    private Double shippingCost;
    /**
     * De som van net commission
     */
    private Double commissionCost;
    /**
     * De som van net Pick & Pack
     */
    private Double pickPackCost;
    /**
     * De som van Inventory cost
     */
    private Double inventoryCost;
    /**
     * De som van (sales price correction - sales price correction VAT)
     */
    private Double returnCost;
    /**
     * De som van Advertisement cost
     */
    private Double advertisingCost;
    /**
     * Omzet - inkoop - verzendkosten - commissiekosten - Pick &pack - voorraadkosten - retourkosten - advertentiekosten
     */
    private Double grossProfit;
    /**
     * de som van VAT
     */
    private Double vat;
    /**
     * Brutowinst - BTW
     */
    private Double netProfit;
    /**
     * (Nettowinst / inkoop) * 100%
     */
    private Double roi;
}