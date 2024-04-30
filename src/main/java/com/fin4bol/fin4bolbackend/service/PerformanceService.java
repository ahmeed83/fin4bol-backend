package com.fin4bol.fin4bolbackend.service;

import com.fin4bol.fin4bolbackend.repository.PerformanceRepository;
import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    public PerformanceService(final PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    public void updateProductName(final ApplicationUser applicationUser,
                                  final String eaNumber,
                                  final String productName,
                                  final Double purchaseCost) {
        final int updated = performanceRepository.updateProductName(applicationUser, eaNumber, productName, purchaseCost);
        if (updated == 0) {
            log.debug("No performance updated for user {} and ean {}", applicationUser.getId(), eaNumber);
        }
    }
}
