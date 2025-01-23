package main.backend.authentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jwt.JwtTokenProvider;
import main.backend.model.entity.UserEntity;
import main.backend.session.CookieHandler;
import main.backend.session.SessionTokenState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// TODO. 访问需要认证的Endpoint, 在认证成功后的处理器
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public AuthenticationSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // TODO. 从认证成功后的结果Authentication中获取UserDetails
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        clearAuthenticationAttributes(request);
        UserEntity user = (UserEntity) authentication.getPrincipal();

        String jwsToken = JwtTokenProvider.generateJwtToken(user.getUsername());

        CookieHandler.addTokenToCookieStoreOfResponse(response, jwsToken);

        SessionTokenState tokenState = new SessionTokenState(jwsToken, 600);
        String jwtResponse = objectMapper.writeValueAsString(tokenState);
        response.setContentType("application/json");
        response.getWriter().write(jwtResponse);
    }
}
