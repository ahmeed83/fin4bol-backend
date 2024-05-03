package com.fin4bol.fin4bolbackend.service;

import com.fin4bol.fin4bolbackend.configuration.security.jwt.JwtTokenVerifier;
import com.fin4bol.fin4bolbackend.controller.json.PerformanceJson;
import com.fin4bol.fin4bolbackend.controller.json.PerformanceRapportJson;
import com.fin4bol.fin4bolbackend.repository.PerformanceRapportRepository;
import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import com.fin4bol.fin4bolbackend.repository.entiry.PerformanceRapport;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class PerformanceReportService {

    private final PerformanceRapportRepository performanceRapportRepository;
    private final ExcelMapperService excelMapperService;
    private final JwtTokenVerifier jwtTokenVerifier;
    private final ApplicationUserService applicationUserService;

    public PerformanceReportService(final PerformanceRapportRepository performanceRepository,
                                    final ExcelMapperService excelMapperService,
                                    final JwtTokenVerifier jwtTokenVerifier,
                                    final ApplicationUserService applicationUserService) {
        this.performanceRapportRepository = performanceRepository;
        this.excelMapperService = excelMapperService;
        this.jwtTokenVerifier = jwtTokenVerifier;
        this.applicationUserService = applicationUserService;
    }

    public PerformanceRapportJson getSpecificationReport(final String token, final String specificationId) {
        final String email = jwtTokenVerifier.extractUserEmail(token);
        final ApplicationUser userByUserName = applicationUserService.findUserByEmail(email);
        final PerformanceRapport performanceRapport =
                performanceRapportRepository.findByUserIdSortedByPerformanceUpdatedAtDesc(UUID.fromString(specificationId), userByUserName)
                        .orElseThrow(() -> new RuntimeException("Specification not found"));
        return mapPerformanceRapportToJson(performanceRapport);
    }

    public List<PerformanceRapportJson> getAllSpecificationReports(final String token) {
        final String email = jwtTokenVerifier.extractUserEmail(token);
        final ApplicationUser userByUserName = applicationUserService.findUserByEmail(email);
        List<PerformanceRapport> performanceRapport =
                performanceRapportRepository.findAllByApplicationUserIdOrderByUpdatedAtDesc(userByUserName)
                        .stream()
                        .limit(5)
                        .toList();
        return performanceRapport.stream().map(this::mapPerformanceRapportListJson).toList();
    }

    public PerformanceRapportJson handleUploadSpecification(final String token, final MultipartFile specification) {
        final String email = jwtTokenVerifier.extractUserEmail(token);
        final ApplicationUser applicationUser = applicationUserService.findUserByEmail(email);
        final PerformanceRapport performanceRapport = excelMapperService.addPerformanceData(applicationUser, specification);
        performanceRapport.setApplicationUserId(applicationUser);
        performanceRapport.setCreatedAt(LocalDateTime.now());
        performanceRapport.setUpdatedAt(LocalDateTime.now());
        performanceRapportRepository.save(performanceRapport);
        return mapPerformanceRapportToJson(performanceRapport);
    }

    @Transactional
    public List<PerformanceRapportJson> deleteSpecificationReport(final String token, final String specificationId) {
        final String email = jwtTokenVerifier.extractUserEmail(token);
        final ApplicationUser userByUserName = applicationUserService.findUserByEmail(email);
        performanceRapportRepository.deleteByIdAndApplicationUserId(UUID.fromString(specificationId), userByUserName);
        List<PerformanceRapport> performanceRapport =
                performanceRapportRepository.findAllByApplicationUserIdOrderByUpdatedAtDesc(userByUserName)
                        .stream()
                        .limit(5)
                        .toList();
        return performanceRapport.stream().map(this::mapPerformanceRapportListJson).toList();
    }

    private PerformanceRapportJson mapPerformanceRapportListJson(PerformanceRapport performanceRapport) {
        PerformanceRapportJson performanceRapportJson = new PerformanceRapportJson();
        performanceRapportJson.setId(performanceRapport.getId());
        performanceRapportJson.setPeriod(performanceRapport.getPeriod());
        performanceRapportJson.setSalespersonNumber(performanceRapport.getSalespersonNumber());
        return performanceRapportJson;
    }

    private PerformanceRapportJson mapPerformanceRapportToJson(PerformanceRapport performanceRapport) {
        PerformanceRapportJson performanceRapportJson = new PerformanceRapportJson();
        performanceRapportJson.setId(performanceRapport.getId());
        performanceRapportJson.setPeriod(performanceRapport.getPeriod());
        performanceRapportJson.setSalespersonNumber(performanceRapport.getSalespersonNumber());
        performanceRapportJson.setPerformanceList(performanceRapport.getPerformanceList().stream()
                .map(performance -> {
                    PerformanceJson performanceJson = new PerformanceJson();
                    performanceJson.setName(performance.getName());
                    performanceJson.setEanNumber(performance.getEanNumber());
                    performanceJson.setPurchaseCost(performance.getPurchaseCost());
                    performanceJson.setAveragePricePerProduct(performance.getAveragePricePerProduct());
                    performanceJson.setRevenue(performance.getRevenue());
                    performanceJson.setSalesVolume(performance.getSalesVolume());
                    performanceJson.setVat(performance.getVat());
                    performanceJson.setCommission(performance.getCommission());
                    performanceJson.setCommissionCorrection(performance.getCommissionCorrection());
                    performanceJson.setLostItemCompensation(performance.getLostItemCompensation());
                    performanceJson.setNetCommission(performance.getNetCommission());
                    performanceJson.setShippingCost(performance.getShippingCost());
                    performanceJson.setShippingCostCorrection(performance.getShippingCostCorrection());
                    performanceJson.setBolComShippingLabelCost(performance.getBolComShippingLabelCost());
                    performanceJson.setTotalShippingCost(performance.getTotalShippingCost());
                    performanceJson.setUnsellableInventoryCost(performance.getUnsellableInventoryCost());
                    performanceJson.setPickPackCost(performance.getPickPackCost());
                    performanceJson.setPickPackCostCorrection(performance.getPickPackCostCorrection());
                    performanceJson.setNetPickPackCost(performance.getNetPickPackCost());
                    performanceJson.setInventoryCost(performance.getInventoryCost());
                    performanceJson.setSalesPriceCorrection(performance.getSalesPriceCorrection());
                    performanceJson.setSalesPriceCorrectionVat(performance.getSalesPriceCorrectionVat());
                    return performanceJson;
                })
                .toList());
        return performanceRapportJson;
    }
}
