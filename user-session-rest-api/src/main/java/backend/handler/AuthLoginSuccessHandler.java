package backend.handler;

import backend.model.entity.UserEntity;
import backend.session.token.TokenState;
import com.fasterxml.jackson.databind.ObjectMapper;
import jwt.JwtTokenProvider;
import backend.session.token.TokenHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// TODO. 定义用户认证成功后的处理器
@Component
public class AuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // TODO. 处理认证成功的UserEntity，创建Session并设置到Response中
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        clearAuthenticationAttributes(request);

        // 从Authentication中拿到验证成功的UserEntity用户信息
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String jwsToken = JwtTokenProvider.generateJwtToken(user.getUsername());

        // 将生成的Session Token设置到Response
        TokenHelper.addTokenToResponse(response, jwsToken);

        ObjectMapper objectMapper = new ObjectMapper();
        TokenState tokenState = new TokenState(user.hasRoleAdmin(), jwsToken, 600);
        String jwtResponse = objectMapper.writeValueAsString(tokenState);
        response.setContentType("application/json");

        response.getWriter().write(jwtResponse);
    }
}
