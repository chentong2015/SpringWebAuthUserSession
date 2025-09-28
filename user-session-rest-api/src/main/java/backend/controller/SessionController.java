package backend.controller;

import backend.session.CookieManager;
import backend.session.CookieTokenHelper;
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

    // TODO. 静态Token的两种更新方式: 保证token能够被刷新
    // - 用户在有效期内请求刷新，可在用户空间发送请求
    // - 用户在有效期后需要重新登录，再来进行刷新
    @GetMapping("/session/refresh")
    public ResponseEntity<?> refreshAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
        String authToken = CookieTokenHelper.fetchToken(request);
        if (authToken == null || !JwtTokenProvider.canTokenBeRefreshed(authToken)) {
            return ResponseEntity.badRequest().build();
        }

        // 将更新后的token设置到response中，返回并设置到cookie中
        String refreshedToken = JwtTokenProvider.refreshJwtToken(authToken);
        CookieTokenHelper.addTokenToResponse(response, refreshedToken);

        return ResponseEntity.ok().build();

        // TokenState tokenState = new TokenState(refreshedToken, 600);
        // return ResponseEntity.ok().body(tokenState);
    }
}
