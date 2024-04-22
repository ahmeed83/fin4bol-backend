package com.fin4bol.fin4bolbackend.exception.security;

import com.fin4bol.fin4bolbackend.exception.ApplicationException;
import org.springframework.http.HttpStatus;

/**
 * Exception for password does not match.
 */
public class PasswordDoesNotMatchException extends ApplicationException {

    public PasswordDoesNotMatchException() {
        super("Password does not match", HttpStatus.BAD_REQUEST);
    }
}
