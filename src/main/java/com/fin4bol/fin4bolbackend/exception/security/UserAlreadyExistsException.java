package com.fin4bol.fin4bolbackend.exception.security;

import com.fin4bol.fin4bolbackend.exception.ApplicationException;
import org.springframework.http.HttpStatus;

/**
 * Exception for user already exists.
 */
public class UserAlreadyExistsException extends ApplicationException {

    public UserAlreadyExistsException() {
        super("User already exists", HttpStatus.CONFLICT);
    }
}
