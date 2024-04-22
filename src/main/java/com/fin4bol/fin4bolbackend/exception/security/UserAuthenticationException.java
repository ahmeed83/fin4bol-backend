package com.fin4bol.fin4bolbackend.exception.security;

import com.fin4bol.fin4bolbackend.exception.ApplicationException;
import org.springframework.http.HttpStatus;

/**
 * Super Exception class for the user management.
 */
public class UserAuthenticationException extends ApplicationException {

    public UserAuthenticationException() {
        super("User authentication failed", HttpStatus.UNAUTHORIZED);
    }
}
