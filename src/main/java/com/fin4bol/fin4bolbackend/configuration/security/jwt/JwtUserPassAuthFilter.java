package com.fin4bol.fin4bolbackend.configuration.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fin4bol.fin4bolbackend.configuration.security.config.JwtConfig;
import com.fin4bol.fin4bolbackend.exception.security.CustomAuthenticationFailureHandler;
import com.fin4bol.fin4bolbackend.service.ApplicationUserService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * This filter will be executed when the user tries to log in. It will generate a token and send it back to the user.
 */
public class JwtUserPassAuthFilter extends UsernamePasswordAuthenticationFilter {

    private static final String FIN_4_BOL_USER_NAME_HEADER = "fin4bol_user_name";
    private static final String FIN_4_BOL_USER_TYPE_HEADER = "fin4bol_user_type";

    private final ApplicationUserService applicationUserService;
    private final AuthenticationManager authenticationManager;
    private final Key key;
    private final JwtConfig jwtConfig;

    public JwtUserPassAuthFilter(final ApplicationUserService applicationUserService,
                                 final AuthenticationManager authenticationManager,
                                 final Key key,
                                 final JwtConfig jwtConfig) {
        this.applicationUserService = applicationUserService;
        this.key = key;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
    }

    /**
     * Attempt to authenticate the user.
     *
     * @param request  req
     * @param response res
     * @return Authentication
     * @throws AuthenticationException AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            final UserPassAuthRequest authenticationRequest =
                    new ObjectMapper().readValue(request.getInputStream(), UserPassAuthRequest.class);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(authenticationRequest.email,
                            authenticationRequest.password);
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            try {
                response.getWriter().write("{\"error\":\"Internal server error - " + e.getMessage() + "\"}");
            } catch (IOException ioException) {
                logger.error("Failed to handle internal server error", ioException);
            }
            throw new AuthenticationServiceException("Error processing request", e);
        }
    }

    /**
     * This method will be executed only if the attempt method successfully ends. This method will build the token and
     * send it back to the customer.
     *
     * @param request    req
     * @param response   res
     * @param chain      chain
     * @param authResult authResult
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        final var tokenPrefix = jwtConfig.getTokenPrefix();
        final var expirationAfterDays = jwtConfig.getTokenExpirationAfterDays();
        final String token = Jwts.builder()
                .subject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (long) expirationAfterDays * 1000 * 60 * 60 * 24))
                .signWith(key)
                .compact();

        authResult.getAuthorities()
                .stream()
                .filter(x -> x.getAuthority() != null && x.getAuthority().length() > 5 && x.getAuthority()
                        .startsWith("ROLE_"))
                .findFirst()
                .ifPresent((role -> response.addHeader(FIN_4_BOL_USER_TYPE_HEADER, role.getAuthority().substring(5).toLowerCase())));
        response.addHeader(AUTHORIZATION, tokenPrefix + token);
        response.addHeader(FIN_4_BOL_USER_NAME_HEADER, applicationUserService.findUserByEmail(authResult.getName()).getName());
    }

    record UserPassAuthRequest(String email, String password) {
    }
}
