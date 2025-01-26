package backend.cookie_session;

import jakarta.servlet.http.HttpServletRequest;

public class TokenProcessor {

    private TokenProcessor() {
    }

    // 从request请求中解析token数据，token可能来自不同位置
    public static String fetchToken(HttpServletRequest request) {
        String authToken = CookieManager.getTokenFromAuthHeader(request);
        if (authToken == null) {
            authToken = CookieManager.getTokenFromCookieStore(request);
        }
        return authToken;
    }
}
