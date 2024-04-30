package com.fin4bol.fin4bolbackend.repository;

import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import com.fin4bol.fin4bolbackend.repository.entiry.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, UUID> {

    /**
     * Update product name.
     *
     * @param applicationUserId application user id
     * @param eanNumber         ean number
     * @param productName       product name
     * @return number of updated performance records
     */
    @Modifying
    @Transactional
    @Query("""
             UPDATE Performance p
             SET p.name = :productName,
             p.purchaseCost = :purchaseCost,
             p.updatedAt = CURRENT_TIMESTAMP
             WHERE p.performanceRapport.applicationUserId = :applicationUserId
             AND p.eanNumber = :eanNumber
            """)
    int updateProductName(@Param("applicationUserId") ApplicationUser applicationUserId,
                          @Param("eanNumber") String eanNumber,
                          @Param("productName") String productName,
                          @Param("purchaseCost") Double purchaseCost);
}