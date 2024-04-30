package com.fin4bol.fin4bolbackend.repository;

import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import com.fin4bol.fin4bolbackend.repository.entiry.PerformanceRapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerformanceRapportRepository extends JpaRepository<PerformanceRapport, UUID> {

    /**
     * Find all performance reports by application user.
     *
     * @param applicationUser application user
     * @return List of performance reports
     */
    List<PerformanceRapport> findAllByApplicationUserIdOrderByUpdatedAt(ApplicationUser applicationUser);

    /**
     * Find the performance report by id and application user.
     *
     * @param id              id
     * @param applicationUser application user
     * @return performance report
     */
    Optional<PerformanceRapport> findByIdAndApplicationUserIdOrderByUpdatedAt(UUID id, ApplicationUser applicationUser);

    /**
     * Delete the performance report by id and application user.
     *
     * @param id              id
     * @param applicationUser application user
     */
    void deleteByIdAndApplicationUserId(UUID id, ApplicationUser applicationUser);
}
