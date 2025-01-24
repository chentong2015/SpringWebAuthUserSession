package backend.auth_handler;

import backend.model.entity.UserEntity;
import backend.cookie_session.SessionTokenState;
import com.fasterxml.jackson.databind.ObjectMapper;
import jwt.JwtTokenProvider;
import backend.cookie_session.CookieManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// TODO. 访问需要认证的Endpoint, 在认证成功后的处理器
@Component
public class AuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public AuthLoginSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // TODO. 处理认证成功的UserEntity，创建Session并设置到Response中
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {
        clearAuthenticationAttributes(request);

        UserEntity user = (UserEntity) authentication.getPrincipal();
        String jwsToken = JwtTokenProvider.generateJwtToken(user.getUsername());

        CookieManager.addTokenToResponse(response, jwsToken);

        SessionTokenState tokenState = new SessionTokenState(jwsToken, 600);
        String jwtResponse = objectMapper.writeValueAsString(tokenState);
        response.setContentType("application/json");
        response.getWriter().write(jwtResponse);
    }
}
