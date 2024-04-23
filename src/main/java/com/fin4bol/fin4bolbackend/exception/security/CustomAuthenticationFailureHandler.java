package com.fin4bol.fin4bolbackend.exception.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    String errorMessage = """
            {
                "timestamp": "%s",
                "errorMessage": "Unauthorized - %s"
            }
            """;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        response.getWriter().write(String.format(errorMessage, LocalDateTime.now().format(dateTimeFormatter), exception.getMessage()));
    }
}
