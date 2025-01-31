package backend.config.handler;

import backend.model.entity.UserEntity;
import backend.model.bean.TokenState;
import com.fasterxml.jackson.databind.ObjectMapper;
import jwt.JwtTokenProvider;
import backend.cookie_session.TokenHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// TODO. 定义用户认证成功后的处理器
@Component
public class AuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public AuthLoginSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // TODO. 处理认证成功的UserEntity，创建Session并设置到Response中
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        clearAuthenticationAttributes(request);

        // 从Authentication中拿到验证成功的UserEntity用户信息
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String jwsToken = JwtTokenProvider.generateJwtToken(user.getUsername());

        // 将生成的Session Token设置到Response
        TokenHelper.addTokenToResponse(response, jwsToken);

        TokenState tokenState = new TokenState(jwsToken, 600);
        String jwtResponse = objectMapper.writeValueAsString(tokenState);
        response.setContentType("application/json");
        response.getWriter().write(jwtResponse);
    }
}
