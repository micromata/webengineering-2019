package com.mlesniak.lecture.backend.authentication;

import com.mlesniak.lecture.backend.model.User;
import com.mlesniak.lecture.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    private UserRepository userRepository;

    // An arbitrary, base64 encoded key with a (byte) length of 32 characters. Under unix, you can execute
    // echo -n "<your key>"|base64 for cleartext keys.
    @Value("${SECRET_KEY}")
    private String secretKey;

    // If set to true, a plain username is expected instead of a signed JWT, i.e.
    //
    //      Bearer suki
    //
    // instead of
    //
    //      Bearer ey......
    //
    // in the HTTP Authorization header.
    @Value("${debug.authentication:false}")
    private boolean debugAuthentication;

    @Autowired
    public AuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void warn() {
        if (debugAuthentication) {
            LOG.warn("Debug authentication is enabled using debug.authentication = true");
        }
    }

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

        if (debugAuthentication) {
            return handleDebugAuthentication(authorization);
        }

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey.getBytes()));
        try {
            Jwt jwt = Jwts.parser().setSigningKey(key).parseClaimsJws(authorization);
            return Optional.of(((DefaultClaims) jwt.getBody()));
        } catch (JwtException e) {
            LOG.warn("Malformed JWT token submitted: <{}>", authorization);
            return Optional.empty();
        }
    }

    private Optional<Claims> handleDebugAuthentication(String userName) {
        // Parse and create a similar Claims object.
        DefaultClaims claims = new DefaultClaims();
        claims.put("sub", userName);
        claims.put("name", "FN/" + userName);

        Optional<User> oUser = userRepository.findByUserName(userName);
        if (!oUser.isPresent()) {
            // We have to create the user to have an id (primary key).
            User user = new User();
            user.setFullName("FN/" + userName);
            user.setUserName(userName);
            userRepository.save(user);
            claims.put("id", user.getId().intValue());
        } else {
            claims.put("id", oUser.get().getId().intValue());
        }

        return Optional.of(claims);
    }
}
