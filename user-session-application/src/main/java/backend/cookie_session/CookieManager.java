package backend.cookie_session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 从Request请求中获取Cookie数据/Token数据
public class CookieManager {

    private static final String AUTH_COOKIE_NAME = "AUTH-TOKEN";
    private static final String SOCIAL_COOKIE = "SOCIAL-MEDIA";
    private static final String ADVERTISING_COOKIE = "FILTER-TYPE";

    private CookieManager() {
    }

    // 从request请求中解析token数据，token可能来自不同位置
    public static String fetchToken(HttpServletRequest request) {
        String authToken = getTokenFromAuthHeader(request);
        if (authToken == null) {
            authToken = getTokenFromCookieStore(request);
        }
        return authToken;
    }

    /**
     *  Getting token from Authentication header, e.g Bearer your_token
     */
    public static String getTokenFromAuthHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     *  Getting token from Cookie store 从请求的Cookie<key, value>信息中获取Token
     */
    public static String getTokenFromCookieStore(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie: request.getCookies()) {
            if (cookie.getName().equals(AUTH_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // TODO. 自定义在Http Response中添加Cookie数据(在请求时自动被携带发送)
    // 1. Tomcat会自动在Response中添加JSESSIONID会话ID
    // 2. Spring Security会自动在Response中添加XSRF-TOKEN随机值
    public static void addTokenToResponse(HttpServletResponse response, String jwtToken) {
        Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, jwtToken);
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        authCookie.setMaxAge(600);

        response.addCookie(authCookie);
    }

    // TODO. 用户选择"Accept All Cookies"后添加其它的Cookie数据
    // Tomcat cookie 值的设置不允许包含空格space (character 32)
    public static void addExtraCookiesToResponse(HttpServletResponse response) {
        Cookie socialMedia = new Cookie(SOCIAL_COOKIE, "By-default-social-media");
        socialMedia.setPath("/");
        socialMedia.setHttpOnly(true);
        socialMedia.setMaxAge(500);

        Cookie advertising = new Cookie(ADVERTISING_COOKIE, "Based-on-user-preference");
        advertising.setPath("/");
        advertising.setHttpOnly(true);
        advertising.setMaxAge(1000);

        response.addCookie(socialMedia);
        response.addCookie(advertising);
    }

    public static String getAuthCookieName() {
        return AUTH_COOKIE_NAME;
    }
}
