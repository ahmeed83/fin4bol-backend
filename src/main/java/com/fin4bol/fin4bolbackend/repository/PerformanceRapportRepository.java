package com.fin4bol.fin4bolbackend.repository;

import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import com.fin4bol.fin4bolbackend.repository.entiry.PerformanceRapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerformanceRapportRepository extends JpaRepository<PerformanceRapport, UUID> {

    /**
     * Find all performance reports by application user sorted by performance updated at descending.
     *
     * @param applicationUser application user
     * @return List of performance reports
     */
    @Query("""
            SELECT pr FROM PerformanceRapport pr
            JOIN FETCH pr.performanceList pl
            WHERE pr.applicationUserId = :applicationUser
            ORDER BY pl.updatedAt DESC
            """)
    List<PerformanceRapport> findAllByApplicationUserIdOrderByUpdatedAtDesc(ApplicationUser applicationUser);

    /**
     * Find the performance report by id and application user sorted by performance updated at descending.
     *
     * @param id              id
     * @param applicationUser application user
     * @return Optional of the performance report
     */
    @Query("""
            SELECT pr FROM PerformanceRapport pr
            JOIN FETCH pr.performanceList pl
            WHERE pr.id = :id
            AND pr.applicationUserId = :applicationUser
            ORDER BY pl.updatedAt DESC
            """)
    Optional<PerformanceRapport> findByUserIdSortedByPerformanceUpdatedAtDesc(UUID id, ApplicationUser applicationUser);

    /**
     * Delete the performance report by id and application user.
     *
     * @param id              id
     * @param applicationUser application user
     */
    void deleteByIdAndApplicationUserId(UUID id, ApplicationUser applicationUser);
}
