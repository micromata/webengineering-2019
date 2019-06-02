package com.mlesniak.lecture.backend.authentication;

import com.mlesniak.lecture.backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    private User user;

    // An arbitrary, base64 encoded key with a (byte) length of 32 characters. Under unix, you can execute
    // echo -n "<your key>"|base64 for cleartext keys.
    @Value("${SECRET_KEY}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;

        // If a JWT token is present, use its values to create a user object. Nevertheless, we do not enforce it
        // for all requests (see below).
        boolean valid = parseToken(httpReq);

        // In our case we do not permit any HTTP requests which are POST.
        if (httpReq.getMethod().equals("POST")) {
            if (!valid) {
                LOG.warn("Unauthorized request to {}", ((HttpServletRequest) req).getRequestURI());
                httpResp.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }

        // We allow everything else.
        chain.doFilter(req, resp);
    }

    private boolean parseToken(HttpServletRequest request) {
        Optional<Claims> claims = decodeRequest(request);
        if (claims.isPresent()) {
            Claims c = claims.get();
            user.setId((long) c.get("id", Integer.class));
            user.setUserName(c.getSubject());
            user.setFullName(c.get("name", String.class));
            LOG.info("claimUser={}", user);
            return true;
        }

        return false;
    }

    public Optional<Claims> decodeRequest(HttpServletRequest request) {
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
