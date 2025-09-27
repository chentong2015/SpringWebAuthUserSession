package backend.controller;

import backend.session.token.TokenState;
import backend.session.CookieManager;
import backend.session.token.TokenHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionController {

    // TODO. 用户自定义选择接受推荐的Cookie信息
    @GetMapping("/cookies/accept")
    public ResponseEntity<?> acceptAllCookies(HttpServletResponse response) {
        CookieManager.addExtraCookiesToResponse(response);
        return ResponseEntity.ok().body("Accept All Cookies Saved !");
    }

    // TODO. 静态Token的两种更新方式
    // - 用户在有效期内请求刷新，可在用户空间发送请求
    // - 用户在有效期后重新Login，获取新的静态Token进行访问
    @GetMapping("/session/refresh")
    public ResponseEntity<?> refreshAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
        String authToken = TokenHelper.fetchToken(request);
        if (authToken == null) {
            TokenState tokenState = new TokenState();
            return ResponseEntity.accepted().body(tokenState);
        }

        String refreshedToken = JwtTokenProvider.refreshJwtToken(authToken);
        TokenHelper.addTokenToResponse(response, refreshedToken);

        TokenState tokenState = new TokenState(refreshedToken, 600);
        return ResponseEntity.ok().body(tokenState);
    }
}
