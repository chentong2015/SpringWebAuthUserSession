package backend.config.filter;

import backend.session.TokenHelper;
import backend.session.token.InvalidAuthenticationToken;
import backend.session.token.BasedTokenAuthentication;
import backend.session.token.ValidAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtTokenProvider;
import backend.model.entity.UserEntity;
import backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// TODO. OncePerRequestFilter 关于API请求的单次过滤器
@Component
public class TokenRequestFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public TokenRequestFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authToken = TokenHelper.fetchToken(request);
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
            // 返回一个能够被认证成功的AuthenticationToken对象
            // 允许用户访问特定路径的URL
            authentication = new ValidAuthenticationToken();
        } else {
            authentication = new InvalidAuthenticationToken();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
