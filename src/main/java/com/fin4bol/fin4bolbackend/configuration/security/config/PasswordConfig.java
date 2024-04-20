package com.fin4bol.fin4bolbackend.configuration.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Password config class.
 */
@Configuration
public class PasswordConfig {

    /**
     * This will encrypt each password and will add {bcrypt}$2y$10$ in the begin of each password.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
