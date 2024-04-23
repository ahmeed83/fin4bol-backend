package com.fin4bol.fin4bolbackend.repository;

import com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserPrincipal;
import com.fin4bol.fin4bolbackend.exception.security.UserAuthenticationException;
import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserRole.ADMIN;
import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserRole.CUSTOMER;
import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserRole.EMPLOYEE;

/**
 * Repository for User Principal interface Impl.
 */
@Repository
public class ApplicationUserPrincipalRepository {

    /**
     * UserRepository.
     */
    private final UserRepository userRepository;

    /**
     * PasswordEncoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor.
     *
     * @param passwordEncoder passwordEncoder
     * @param userRepository  userRepository
     */
    @Autowired
    public ApplicationUserPrincipalRepository(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Find the user by its name. This method will be used by Spring to load the user from the database.
     *
     * @param email email
     * @return ApplicationUserPrincipal
     */
    public Optional<ApplicationUserPrincipal> findUserByEmail(String email) {
        final var applicationUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        Set<SimpleGrantedAuthority> grantedAuthority = switch (applicationUser.getRole()) {
            case "ADMIN" -> ADMIN.getGrantedAuthority();
            case "EMPLOYEE" -> EMPLOYEE.getGrantedAuthority();
            case "CUSTOMER" -> CUSTOMER.getGrantedAuthority();
            default -> throw new IllegalStateException("Unknown Role name: " + applicationUser.getRole());
        };
        return Optional.of(new ApplicationUserPrincipal(applicationUser.getEmail(), applicationUser.getPassword(),
                grantedAuthority, applicationUser.isEnabled()));
    }

    /**
     * This method will save a new user to the database. Each user will be enabled by default. Each user will have the
     * role customer by default.
     *
     * @param applicationUser user that will be saved
     * @throws UserAuthenticationException user exception
     */
    public void saveApplicationUser(ApplicationUser applicationUser) throws UserAuthenticationException {
        final LocalDateTime now = LocalDateTime.now();
        applicationUser.setCreatedAt(now);
        applicationUser.setUpdatedAt(now);
        applicationUser.setEnabled(true);
        applicationUser.setPassword(passwordEncoder.encode(applicationUser.getPassword()));
        applicationUser.setRole(CUSTOMER.getRole());
        userRepository.save(applicationUser);
    }
}