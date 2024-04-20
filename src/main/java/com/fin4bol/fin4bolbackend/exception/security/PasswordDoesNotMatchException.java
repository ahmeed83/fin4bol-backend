package com.fin4bol.fin4bolbackend.exception.security;

/**
 * Exception for password does not match.
 */
public class PasswordDoesNotMatchException extends UserAuthenticationException {

    public PasswordDoesNotMatchException() {
        super("Password does not match");
    }
}
