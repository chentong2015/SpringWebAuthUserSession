package org.example.util;

import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.util.UriTemplate;

import java.util.Map;

public class UrlPathParser {

    private UrlPathParser() {
    }

    // Extract the userId from the request URI: /users/{userId}
    public static String parseUsername(RequestAuthorizationContext context) {
        String url = context.getRequest().getRequestURI();

        UriTemplate uriTemplate = new UriTemplate("/users/{username}");
        Map<String, String> uriVariables = uriTemplate.match(url);
        return uriVariables.get("username");
    }
}
