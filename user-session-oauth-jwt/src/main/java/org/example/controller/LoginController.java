package org.example.controller;

import org.example.model.Response;
import org.example.model.StatusCode;
import org.example.service.LoginService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class LoginController {

    private final LoginService authService;

    public LoginController(LoginService authService) {
        this.authService = authService;
    }

    // TODO. 验证和操作流程: 只有authentication成功，请求发送到该Endpoint
    // 1. BasicAuthenticationFilter
    //    processing any request that has an HTTP request header of Authorization
    //    with an authentication scheme of Basic and a Base64-encoded username:password token.
    // 2. BasicAuthenticationFilter
    //    prepare the Authentication object for this login method.
    // 3. Spring Security authenticated the username and password through Basic Auth.
    //    before this login method gets called
    @PostMapping("/login")
    public Response getLoginInfo(Authentication authentication) {
        System.out.println("Authenticated user: " + authentication.getName());
        Map<String, Object> loginInfo = this.authService.createLoginInfo(authentication);

        String message = "User Info and JSON Web Token";
        return new Response(StatusCode.SUCCESS, message, loginInfo);
    }
}