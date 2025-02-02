package org.example.controller;

import org.example.model.Response;
import org.example.model.StatusCode;
import org.example.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Generate a JSON web token if username and password has been authenticated by the BasicAuthenticationFilter.
     * In summary, this filter is responsible for processing any request that has an HTTP request header of Authorization
     * with an authentication scheme of Basic and a Base64-encoded username:password token.
     * <p>
     * BasicAuthenticationFilter will prepare the Authentication object for this login method.
     * Note: before this login method gets called, Spring Security already authenticated the username and password through Basic Auth.
     * Only successful authentication can make it to this method.
     *
     * @return User information and JSON web token
     */
    @PostMapping("/login")
    public Response getLoginInfo(Authentication authentication) {
        System.out.println("Authenticated user: " + authentication.getName());
        Map<String, Object> loginInfo = this.authService.createLoginInfo(authentication);
        return new Response(StatusCode.SUCCESS, "User Info and JSON Web Token", loginInfo);
    }
}
