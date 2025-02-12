package backend.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieManager {

    private static final String SOCIAL_COOKIE = "SOCIAL-MEDIA";
    private static final String ADVERTISING_COOKIE = "FILTER-TYPE";

    private CookieManager() {
    }

    // 为请求添加特定Filter: 获取请求中特定Cookies数据
    public static void parseCookiesFromRequest(HttpServletRequest request) {
        Cookie[] allCookies = request.getCookies();
        if (allCookies != null) {
            for (Cookie cookie: allCookies) {
                // Check for Tomcat Session
                if (cookie.getName().equals("JSESSIONID")) {
                    cookie.setMaxAge(100); // 100s
                    cookie.setHttpOnly(false);
                    cookie.setSecure(true);
                }
            }
        }
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
}
