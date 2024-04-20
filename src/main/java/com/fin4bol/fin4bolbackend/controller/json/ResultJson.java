package com.fin4bol.fin4bolbackend.controller.json;

import lombok.Data;

@Data
public class ResultJson {
    private Double sales;
    private Double revenue;
    private Double purchaseCost;
    private Double shippingCost;
    private Double commissionCost;
    private Double pickPackCost;
    private Double inventoryCost;
    private Double returnCost;
    private Double advertisingCost;
    private Double grossProfit;
    private Double vat;
    private Double netProfit;
    private Double roi;
}
