package com.fin4bol.fin4bolbackend.repository.entiry;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "performance")
@Getter
@Setter
public class Performance extends BaseModel {

    private String name;
    @Column(name = "ean", length = 20)
    private String eanNumber;
    private Double purchaseCost;
    private Double averagePricePerProduct;
    private Double revenue;
    private Integer salesVolume;
    private Double vat;
    private Double commission;
    private Double commissionCorrection;
    private Double lostItemCompensation;
    private Double netCommission;
    private Double shippingCost;
    private Double shippingCostCorrection;
    private Double bolComShippingLabelCost;
    private Double totalShippingCost;
    private Double unsellableInventoryCost;
    private Double pickPackCost;
    private Double pickPackCostCorrection;
    private Double netPickPackCost;
    private Double inventoryCost;
    private Double salesPriceCorrection;
    private Double salesPriceCorrectionVat;

    @ManyToOne
    @JoinColumn(name = "performance_rapport_id", referencedColumnName = "id")
    private PerformanceRapport performanceRapport;
}

