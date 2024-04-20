package com.fin4bol.fin4bolbackend.repository;

import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository class for the user.
 */
@Repository
public interface UserRepository extends CrudRepository<ApplicationUser, UUID> {

    /**
     * Find user by name in the database.
     *
     * @param userName userName
     * @return ApplicationUser
     */
    Optional<ApplicationUser> findByUserName(String userName);
}
