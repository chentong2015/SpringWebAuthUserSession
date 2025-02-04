package org.example.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.InMemoryCacheService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final InMemoryCacheService cacheService;

    public JwtInterceptor(InMemoryCacheService cacheService) {
        this.cacheService = cacheService;
    }

    // Check Authorization header and compare token with the date in Cache
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) authentication.getPrincipal();

            String token = jwt.getTokenValue();
            String username = jwt.getClaim("username").toString();
            if (!this.cacheService.existToken(username, token)) {
                throw new BadCredentialsException("Invalid token");
            }
        }
        // Else this request is a public request without token
        return true;
    }
}
