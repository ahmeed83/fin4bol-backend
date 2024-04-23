package com.fin4bol.fin4bolbackend.repository.entiry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ean", "app_user_id"})
})
@Getter
@Setter
public class Product extends BaseModel {

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "ean", nullable = false, length = 20)
    private String eanNumber;

    @Column(name = "purchaseCost", length = 10, nullable = false)
    private Double purchaseCost; // Inkoopkosten

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private ApplicationUser applicationUserId;
}

