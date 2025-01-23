package main.backend.authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtTokenProvider;
import main.backend.authentication.EmptyBasedAuthentication;
import main.backend.authentication.TokenBasedAuthentication;
import main.backend.model.entity.UserEntity;
import main.backend.repository.UserRepository;
import main.backend.session.CookieHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// TODO. 用户请求一个需要认证的Endpoint, 并且提供了Credentials
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public TokenAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authToken = CookieHandler.getTokenFromAuthHeader(request);
        if (authToken == null) {
            // 提供一个能够通过认证的Empty Authentication
            SecurityContextHolder.getContext().setAuthentication(new EmptyBasedAuthentication());
        }

        String username = JwtTokenProvider.getUserNameFromJwtToken(authToken);
        UserEntity userEntity = this.userRepository.findByUsername(username).orElse(null);
        if (userEntity == null) {
            // 提供了正确签名的Token, 但是DB中却没有该用户信息
            throw new UsernameNotFoundException("No user found");
        }

        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userEntity, authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
