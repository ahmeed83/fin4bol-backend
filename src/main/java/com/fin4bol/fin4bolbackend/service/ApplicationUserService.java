package com.fin4bol.fin4bolbackend.service;

import com.fin4bol.fin4bolbackend.configuration.security.jwt.json.ApplicationUserJson;
import com.fin4bol.fin4bolbackend.exception.security.PasswordDoesNotMatchException;
import com.fin4bol.fin4bolbackend.exception.security.UserAlreadyExistsException;
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
     * @param email email
     * @return the loaded user
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return applicationUserPrincipalRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User email %s not found", email)));
    }

    /**
     * Register a new user in the database
     *
     * @param applicationUser the new user
     */
    public void saveApplicationUser(ApplicationUserJson applicationUser) throws UserAuthenticationException {
        if (userRepository.findByEmail(applicationUser.getEmail().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        if (!applicationUser.getPassword().equals(applicationUser.getPasswordConfirm())) {
            throw new PasswordDoesNotMatchException();
        }
        ApplicationUser user = ApplicationUser.builder()
                .email(applicationUser.getEmail())
                .password(applicationUser.getPassword())
                .name(applicationUser.getName() == null || applicationUser.getName().isEmpty()
                        ? applicationUser.getEmail() : applicationUser.getName())
                .referralSource(applicationUser.getReferralSource())
                .build();
        applicationUserPrincipalRepository.saveApplicationUser(user);
    }

    /**
     * Find user by userName
     *
     * @param userName the userName
     * @return the user
     */
    public ApplicationUser findUserByEmail(String userName) {
        return userRepository.findByEmail(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
    }
}
