package com.fin4bol.fin4bolbackend.configuration.security.jwt;

import com.fin4bol.fin4bolbackend.configuration.security.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * JWT token verifier.
 * This filter will be executed for every request and added to the req filter.
 */
@Service
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtTokenVerifier(JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    /**
     * Verify the user for each request.
     *
     * @param httpServletRequest  req
     * @param httpServletResponse res
     * @param filterChain         filter chain
     * @throws ServletException ServletException
     * @throws IOException      IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final var tokenPrefix = jwtConfig.getTokenPrefix();
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);

        if (isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        final String token = authorizationHeader.replace(tokenPrefix, "");
        try {
            final Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            final Claims body = claimsJws.getPayload();
            final String username = body.getSubject();
            @SuppressWarnings("unchecked") final var authorities = (List<Map<String, String>>) body.get("authorities");
            final var simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                    simpleGrantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s can not be trusted!", token));
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * Extract the username from the token.
     *
     * @param bearerToken token
     * @return user name
     */
    public String extractUserName(final String bearerToken) {
        final var tokenPrefix = jwtConfig.getTokenPrefix();
        final String token = bearerToken.replace(tokenPrefix, "");
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private boolean isNullOrEmpty(final String authorizationHeader) {
        return authorizationHeader == null || authorizationHeader.isEmpty();
    }
}
