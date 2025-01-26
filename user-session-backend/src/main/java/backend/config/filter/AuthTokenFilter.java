package backend.config.filter;

import backend.cookie_session.TokenProcessor;
import backend.model.InvalidAuthentication;
import backend.model.BasedTokenAuthentication;
import backend.model.ValidAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtTokenProvider;
import backend.model.entity.UserEntity;
import backend.repository.UserRepository;
import backend.cookie_session.CookieManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// TODO. 用户请求一个需要认证的Endpoint, 并且提供了Credentials
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public AuthTokenFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authToken = TokenProcessor.fetchToken(request);
        Authentication authentication;
        if (authToken != null) {
            // 从token中解析用户名称，验证token的有效性
            String username = JwtTokenProvider.getUsernameFromJwtToken(authToken);
            UserEntity userEntity = this.userRepository.findByUsername(username).orElse(null);
            if (userEntity == null) {
                throw new UsernameNotFoundException("No user found");
            }
            authentication = new BasedTokenAuthentication(userEntity, authToken);
        } else if (WebPageFilter.isValidPath(request)) {
            authentication = new ValidAuthentication();
        } else {
            authentication = new InvalidAuthentication();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
