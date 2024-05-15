package com.fin4bol.fin4bolbackend.service;

import com.fin4bol.fin4bolbackend.configuration.security.jwt.JwtTokenVerifier;
import com.fin4bol.fin4bolbackend.controller.json.TotalOverviewJson;
import com.fin4bol.fin4bolbackend.repository.PerformanceRapportRepository;
import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import com.fin4bol.fin4bolbackend.repository.entiry.Performance;
import com.fin4bol.fin4bolbackend.repository.entiry.PerformanceRapport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TotalOverviewService {

    private final JwtTokenVerifier jwtTokenVerifier;
    private final ApplicationUserService applicationUserService;
    private final PerformanceRapportRepository performanceRapportRepository;

    public TotalOverviewService(final JwtTokenVerifier jwtTokenVerifier,
                                final ApplicationUserService applicationUserService,
                                final PerformanceRapportRepository performanceRapportRepository) {
        this.jwtTokenVerifier = jwtTokenVerifier;
        this.applicationUserService = applicationUserService;
        this.performanceRapportRepository = performanceRapportRepository;
    }

    public TotalOverviewJson getTotalOverview(final String token, final String specificationId) {
        final String email = jwtTokenVerifier.extractUserEmail(token);
        final ApplicationUser userByUserName = applicationUserService.findUserByEmail(email);
        final PerformanceRapport performanceRapport =
                performanceRapportRepository.findByUserIdSortedByPerformanceUpdatedAtDesc(UUID.fromString(specificationId), userByUserName)
                        .orElseThrow(() -> new RuntimeException("Specification not found"));
        final TotalOverviewJson totalOverviewJson = new TotalOverviewJson();
        totalOverviewJson.setSales(performanceRapport.getPerformanceList()
                .stream().map(Performance::getSalesVolume).reduce(0, Integer::sum));
        totalOverviewJson.setRevenue(performanceRapport.getPerformanceList()
                .stream().map(Performance::getRevenue).reduce(0.0, Double::sum));
        totalOverviewJson.setPurchaseCost(getPurchaseCost(performanceRapport));
        totalOverviewJson.setShippingCost(performanceRapport.getPerformanceList()
                .stream().map(Performance::getTotalShippingCost).reduce(0.0, Double::sum));
        totalOverviewJson.setCommissionCost(performanceRapport.getPerformanceList()
                .stream().map(Performance::getNetCommission).reduce(0.0, Double::sum));
        totalOverviewJson.setPickPackCost(performanceRapport.getPerformanceList()
                .stream().map(Performance::getNetPickPackCost).reduce(0.0, Double::sum));
        totalOverviewJson.setInventoryCost(performanceRapport.getPerformanceList()
                .stream().map(Performance::getInventoryCost).reduce(0.0, Double::sum));
        totalOverviewJson.setReturnCost(getReturnCost(performanceRapport));
        //TODO: add advertisingCost
        totalOverviewJson.setAdvertisingCost(0.0);
        totalOverviewJson.setGrossProfit(getGrossProfit(performanceRapport));
        totalOverviewJson.setVat(performanceRapport.getPerformanceList()
                .stream().map(Performance::getVat).reduce(0.0, Double::sum));
        totalOverviewJson.setNetProfit(totalOverviewJson.getGrossProfit() - totalOverviewJson.getVat());
        totalOverviewJson.setRoi(totalOverviewJson.getPurchaseCost() > 0 ? totalOverviewJson.getNetProfit() / totalOverviewJson.getPurchaseCost() : 0.0);
        return totalOverviewJson;
    }

    private Double getGrossProfit(final PerformanceRapport performanceRapport) {
        return performanceRapport.getPerformanceList()
                .stream().map(performance -> {
                    final Double revenue = performance.getRevenue();
                    final Double purchasePrice = performance.getPurchaseCost();
                    final Double shippingCost = performance.getTotalShippingCost();
                    final Double commissionCost = performance.getNetCommission();
                    final Double pickPackCost = performance.getNetPickPackCost();
                    final Double inventoryCost = performance.getInventoryCost();
                    final Double returnCost = performance.getSalesPriceCorrection() - performance.getSalesPriceCorrectionVat();
                    //TODO: add advertisingCost
                    return revenue - purchasePrice - shippingCost - commissionCost - pickPackCost - inventoryCost - returnCost;
                }).reduce(0.0, Double::sum);
    }

    private Double getReturnCost(final PerformanceRapport performanceRapport) {
        return performanceRapport.getPerformanceList()
                .stream().map(performance -> {
                    final Double salesPriceCorrection = performance.getSalesPriceCorrection();
                    final Double salesPriceCorrectionVat = performance.getSalesPriceCorrectionVat();
                    return salesPriceCorrection - salesPriceCorrectionVat;
                }).reduce(0.0, Double::sum);
    }

    private Double getPurchaseCost(final PerformanceRapport performanceRapport) {
        return performanceRapport.getPerformanceList()
                .stream().map(performance -> {
                    final Double purchasePrice = performance.getPurchaseCost();
                    final Integer salesVolume = performance.getSalesVolume();
                    return purchasePrice * salesVolume;
                }).reduce(0.0, Double::sum);
    }
}
