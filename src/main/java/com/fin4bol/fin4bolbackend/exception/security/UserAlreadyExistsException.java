package com.fin4bol.fin4bolbackend.exception.security;

/**
 * Exception for user already exists.
 */
public class UserAlreadyExistsException extends UserAuthenticationException {

    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
