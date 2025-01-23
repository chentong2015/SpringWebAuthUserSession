package backend.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtTokenProvider;
import backend.model.entity.UserEntity;
import backend.repository.UserRepository;
import backend.session.CookieManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// TODO. 用户请求一个需要认证的Endpoint, 并且提供了Credentials
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public AuthenticationTokenFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authToken = CookieManager.getTokenFromAuthHeader(request);
        if (authToken != null) {
            // 从Token中解析用户名称，保证签名正确并用户存在
            String username = JwtTokenProvider.getUsernameFromJwtToken(authToken);
            UserEntity userEntity = this.userRepository.findByUsername(username).orElse(null);
            if (userEntity == null) {
                throw new UsernameNotFoundException("No user found");
            }

            TokenBasedAuthentication authentication = new TokenBasedAuthentication(userEntity, authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // 提供一个能够通过认证的Empty Authentication
            SecurityContextHolder.getContext().setAuthentication(new EmptyBasedAuthentication());
        }

        chain.doFilter(request, response);
    }
}
