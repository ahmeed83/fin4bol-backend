package com.fin4bol.fin4bolbackend.exception.security;

/**
 * Super Exception class for the user management.
 */
public class UserAuthenticationException extends Exception {

    private final String message;

    public UserAuthenticationException(String message) {
        this.message = message;
    }

    public UserAuthenticationException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
