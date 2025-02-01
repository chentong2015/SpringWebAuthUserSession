package backend.cookie_session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 从Request请求中获取Cookie数据/Token数据
public class TokenHelper {

    private static final String AUTH_TOKEN_NANE = "AUTH-TOKEN";

    private TokenHelper() {
    }

    // 从request请求中解析token数据，token可能来自不同位置
    public static String fetchToken(HttpServletRequest request) {
        String authToken = getTokenFromAuthHeader(request);
        if (authToken == null) {
            authToken = getTokenFromCookieStore(request);
        }
        return authToken;
    }

    // Getting token from Authentication header, e.g Bearer your_token
    public static String getTokenFromAuthHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Getting token from Cookie store 从请求的Cookie<key, value>信息中获取Token
    public static String getTokenFromCookieStore(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie: request.getCookies()) {
            if (cookie.getName().equals(AUTH_TOKEN_NANE)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // TODO. 自定义在Http Response中添加Cookie数据(在请求时自动被携带发送)
    // 1. Tomcat会自动在Response中添加JSESSIONID会话ID
    // 2. Spring Security会自动在Response中添加XSRF-TOKEN随机值
    public static void addTokenToResponse(HttpServletResponse response, String jwtToken) {
        Cookie authCookie = new Cookie(AUTH_TOKEN_NANE, jwtToken);
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        authCookie.setMaxAge(600);

        response.addCookie(authCookie);
    }

    public static String getAuthCookieName() {
        return AUTH_TOKEN_NANE;
    }
}
