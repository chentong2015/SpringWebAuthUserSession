package backend.session;

import backend.session.auth.InvalidAuthenticationToken;
import backend.session.auth.BasedTokenAuthentication;
import backend.session.auth.ValidAuthenticationToken;
import backend.session.token.TokenHelper;
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

// TODO. OncePerRequestFilter 关于API请求的单次过滤器(Web安全的核心)
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
        } else if (isValidPath(request)) {
            // 返回一个能够被认证成功的AuthenticationToken对象
            authentication = new ValidAuthenticationToken();
        } else {
            authentication = new InvalidAuthenticationToken();
        }

        // TODO. 将授权后的实例对象存储到SecurityContextHolder中，用于在系统中获取
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    // 允许用户访问特定路径的URL
    private boolean isValidPath(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.endsWith(".html") || url.endsWith(".js");
    }
}
