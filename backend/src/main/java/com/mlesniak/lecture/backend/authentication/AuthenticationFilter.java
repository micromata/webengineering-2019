package com.mlesniak.lecture.backend.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            String authorization = httpReq.getHeader("Authorization");
            if (StringUtils.isEmpty(authorization)) {
                LOG.warn("Unauthorized request to {}", ((HttpServletRequest) req).getRequestURI());
                ((HttpServletResponse) resp).setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }

        // We allow everything else.
        chain.doFilter(req, resp);
    }
}
