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

    List<PerformanceRapport> findAllByApplicationUserId(ApplicationUser applicationUser);

    Optional<PerformanceRapport> findByIdAndApplicationUserId(UUID id, ApplicationUser applicationUser);

    void deleteByIdAndApplicationUserId(UUID id, ApplicationUser applicationUser);
}
