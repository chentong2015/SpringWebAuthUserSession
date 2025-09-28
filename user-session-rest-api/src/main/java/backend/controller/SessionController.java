package backend.controller;

import backend.model.entity.UserEntity;
import backend.session.token.TokenState;
import backend.session.CookieManager;
import backend.session.token.TokenHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String authToken = TokenHelper.fetchToken(request);
        if (authToken == null || !JwtTokenProvider.canTokenBeRefreshed(authToken)) {
            return ResponseEntity.badRequest().build();
        }

        // 将更新后的token设置到response中，返回并设置到cookie中
        String refreshedToken = JwtTokenProvider.refreshJwtToken(authToken);
        TokenHelper.addTokenToResponse(response, refreshedToken);

        // 判断当前认证的用户是否具有Admin角色
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        TokenState tokenState = new TokenState(user.hasRoleAdmin(), refreshedToken, 600);
        return ResponseEntity.ok().body(tokenState);
    }
}
