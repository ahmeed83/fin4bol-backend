package com.fin4bol.fin4bolbackend.service;

import com.fin4bol.fin4bolbackend.exception.security.UserAuthenticationException;
import com.fin4bol.fin4bolbackend.exception.security.UserNotFoundException;
import com.fin4bol.fin4bolbackend.repository.ApplicationUserPrincipalRepository;
import com.fin4bol.fin4bolbackend.repository.UserRepository;
import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Application user service.
 */
@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserPrincipalRepository applicationUserPrincipalRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserPrincipalRepository applicationUserPrincipalRepository,
                                  final UserRepository userRepository) {
        this.applicationUserPrincipalRepository = applicationUserPrincipalRepository;
        this.userRepository = userRepository;
    }

    /**
     * Load the user form the database
     *
     * @param userName userName
     * @return the loaded user
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return applicationUserPrincipalRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("UserName %s not found", userName)));
    }

    /**
     * Register a new user in the database
     *
     * @param applicationUser the new user
     */
    public void saveApplicationUser(ApplicationUser applicationUser) throws UserAuthenticationException {
        applicationUserPrincipalRepository.saveApplicationUser(applicationUser);
    }

    /**
     * Find user by userName
     *
     * @param userName the userName
     * @return the user
     */
    public ApplicationUser findUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
    }
}
