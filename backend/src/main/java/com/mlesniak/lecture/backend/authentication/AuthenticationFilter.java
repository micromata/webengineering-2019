package com.mlesniak.lecture.backend.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Component
public class AuthenticationFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;

        // In our case we do not permit any HTTP requests which are POST.
        if (httpReq.getMethod().equals("POST")) {
            if (!isAuthorized(httpReq)) {
                LOG.warn("Unauthorized request to {}", ((HttpServletRequest) req).getRequestURI());
                httpResp.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }

        // We allow everything else.
        chain.doFilter(req, resp);
    }

    private boolean isAuthorized(HttpServletRequest request) {
        // TODO ML Add user to context.
        Optional<Claims> claims = decodeRequest(request);
        LOG.info("Claims: {}", claims);
        return claims.isPresent();
    }

    public Optional<Claims> decodeRequest(HttpServletRequest request) {
        // TODO ML Use environment variable for key.
        // Base64 encoded qwertyuiopasdfghjklzxcvbnm123456
        String secretKey = "cXdlcnR5dWlvcGFzZGZnaGprbHp4Y3Zibm0xMjM0NTY=";

        // Use header information from header 'Authorization' to extract user from JWT.
        String authorization = request.getHeader("Authorization");

        int minStringLength = "Bearer ".length();
        if (authorization == null || authorization.length() < minStringLength) {
            return Optional.empty();
        }
        authorization = authorization.substring(minStringLength);
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey.getBytes()));
        try {
            Jwt jwt = Jwts.parser().setSigningKey(key).parseClaimsJws(authorization);
            return Optional.of(((DefaultClaims) jwt.getBody()));
        } catch (JwtException e) {
            LOG.warn("Malformed JWT token submitted: <{}>", authorization);
            return Optional.empty();
        }
    }
}
