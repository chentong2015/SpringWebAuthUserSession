package backend.config.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 过滤特殊的路径的请求
// The below paths will get ignored by the filter
public class WebPageFilter {

    public static final String FAVICON_MATCHER = "/favicon.ico";
    public static final String HTML_MATCHER = "/**/*.html";
    public static final String CSS_MATCHER = "/**/*.css";
    public static final String JS_MATCHER = "/**/*.js";
    public static final String IMG_MATCHER = "/images/*";

    private final List<String> pathsToSkip = Arrays.asList(
            HTML_MATCHER,
            FAVICON_MATCHER,
            CSS_MATCHER,
            JS_MATCHER,
            IMG_MATCHER
    );

    private boolean skipPathRequest(HttpServletRequest request, List<String> pathsToSkip) {
        List<RequestMatcher> m = pathsToSkip.stream()
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        OrRequestMatcher matchers = new OrRequestMatcher(m);
        return matchers.matches(request);
    }

    // 过滤认证所有静态html页面的访问
    public static boolean isValidPath(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.endsWith(".html") || url.endsWith(".js");
    }
}
