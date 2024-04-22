package com.fin4bol.fin4bolbackend.exception.security;

import com.fin4bol.fin4bolbackend.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import static java.lang.String.format;

/**
 * Exception for user is not found.
 */
public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException(String username) {
        super(format("User with username %s not found", username), HttpStatus.NOT_FOUND);
    }
}
