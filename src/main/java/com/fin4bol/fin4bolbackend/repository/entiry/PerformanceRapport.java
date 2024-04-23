package com.fin4bol.fin4bolbackend.repository.entiry;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "performance_rapport")
@Getter
@Setter
public class PerformanceRapport extends BaseModel {

    @Column(name = "period", length = 200, nullable = false)
    private String period;

    @Column(name = "salesperson_number", length = 200, nullable = false)
    private String salespersonNumber; // Verkoper nummer

    @OneToMany(mappedBy = "performanceRapport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Performance> performanceList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private ApplicationUser applicationUserId;
}
