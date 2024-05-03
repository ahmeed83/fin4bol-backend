package com.fin4bol.fin4bolbackend.repository;


import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import com.fin4bol.fin4bolbackend.repository.entiry.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Find products by application user.
     *
     * @param applicationUser application user
     * @return List of products
     */
    Optional<List<Product>> findByApplicationUserIdOrderByUpdatedAtDesc(ApplicationUser applicationUser);

    /**
     * Delete products by application user.
     *
     * @param applicationUser application user
     */
    void deleteProductsByApplicationUserIdAndEanNumber(ApplicationUser applicationUser, String eanNumber);

    /**
     * Find product by application user and ean number.
     *
     * @param applicationUser application user
     * @param eanNumber       ean number
     * @return product
     */
    Optional<Product> findByApplicationUserIdAndEanNumberOrderByUpdatedAtDesc(ApplicationUser applicationUser, String eanNumber);

    /**
     * Find product by ean number.
     *
     * @param eanNumber ean number
     * @return product
     */
    Optional<Product> findByEanNumber(String eanNumber);
}
