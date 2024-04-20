package com.fin4bol.fin4bolbackend.exception.security;

/**
 * Exception for user is not found.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userName) {
        super(String.format("User not found with name: %s", userName));
    }
}
