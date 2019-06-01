package com.mlesniak.lecture.backend.authentication;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        // In our case we do not permit any HTTP requests which are POST.
        HttpServletRequest httpReq = (HttpServletRequest) req;
        if (httpReq.getMethod().equals("POST")) {
            System.out.println("POST");
        }

        chain.doFilter(req, resp);
    }
}
