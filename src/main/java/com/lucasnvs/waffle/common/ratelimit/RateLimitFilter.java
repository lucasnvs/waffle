package com.lucasnvs.waffle.common.ratelimit;

import com.lucasnvs.waffle.auth.domain.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final AuthenticationService authenticationService;

    public RateLimitFilter(RateLimitService rateLimitService, AuthenticationService authenticationService) {
        this.rateLimitService = rateLimitService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        // Skip rate limiting for Swagger UI, API docs, actuator and static resources
        if (uri.startsWith("/swagger-ui") ||
            uri.startsWith("/v3/api-docs") ||
            uri.startsWith("/actuator") ||
            uri.startsWith("/firebase-auth-demo.html") ||
            !uri.startsWith("/api/v1/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId = authenticationService.getCurrentUserId().orElse(null);

        if (userId == null || userId.isBlank()) {
            userId = request.getRemoteAddr();
        }

        String key = "rate_limit:" + userId;

        if (!rateLimitService.allowRequest(key)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

