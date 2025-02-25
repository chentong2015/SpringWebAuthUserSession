package org.example.interceptor;

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

    // TODO. 验证请求的Username和提供的Token是否在缓存中生成过
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String tokenRequest = header.substring(7);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) authentication.getPrincipal();

            String token = jwt.getTokenValue();
            if (!tokenRequest.equals(token)) {
                throw new BadCredentialsException("Request Token is not the same");
            }

            String username = jwt.getClaim("username").toString();
            if (!this.cacheService.existToken(username, token)) {
                throw new BadCredentialsException("Invalid token");
            }
        }
        // For public request without token
        return true;
    }
}
