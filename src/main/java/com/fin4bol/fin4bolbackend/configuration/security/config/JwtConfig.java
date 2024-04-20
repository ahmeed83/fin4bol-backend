package com.fin4bol.fin4bolbackend.configuration.security.config;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

/**
 * JWT config class.
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;

    @Getter
    private String tokenPrefix;

    @Getter
    private int tokenExpirationAfterDays;

    /**
     *  Creates a new SecretKey instance for use with HMAC-SHA algorithms based on the specified key byte array.
     *  Returns: a new SecretKey instance
     *  Throws: WeakKeyException –
     *  if the key byte array length is less than 256 bits (32 bytes) as mandated by the JWT specification.
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}