package backend.cookie_session;

import backend.model.bean.TokenState;
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

    // 必须完成用户认证后才能访问, 用户自定义选择Cookie信息的设置
    @GetMapping("/cookies/accept")
    public ResponseEntity<?> acceptAllCookies(HttpServletResponse response) {
        CookieManager.addExtraCookiesToResponse(response);
        return ResponseEntity.ok().body("Accept All Cookies Saved !");
    }

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
