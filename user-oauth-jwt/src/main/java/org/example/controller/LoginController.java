package org.example.controller;

import org.example.model.CustomUser;
import org.example.model.Response;
import org.example.model.StatusCode;
import org.example.service.LoginService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final LoginService authService;
    private final InMemoryUserDetailsManager inMemoryUserDetails;

    public LoginController(LoginService authService, PasswordEncoder passwordEncoder, InMemoryUserDetailsManager inMemoryCacheService) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.inMemoryUserDetails = inMemoryCacheService;
    }

    // TODO. Login用户验证流程: 只有authentication成功，请求才被发送到该Endpoint
    // 1. BasicAuthenticationFilter processing any request that has an HTTP request header of Authorization
    //    with an authentication scheme of Basic and a Base64-encoded username:password token.
    // 2. Valid the user details
    // 3. BasicAuthenticationFilter prepare the Authentication object for this login method.
    @PostMapping("/login")
    public Response login(Authentication authentication) {
        System.out.println("Authenticated user: " + authentication.getName());
        Map<String, Object> loginInfo = this.authService.createLoginInfo(authentication);

        String message = "User Info and JSON Web Token";
        return new Response(StatusCode.SUCCESS, message, loginInfo);
    }

    // TODO. 暴露用户注册的Endpoint API接口, 支持动态注册新用户
    @PostMapping("/register")
    public Response registerUser(@RequestBody CustomUser customUser) {
        UserDetails newUser = User.builder()
                .username(customUser.getUsername())
                .password(this.passwordEncoder.encode(customUser.getPassword()))
                .roles(customUser.getRole())
                .build();
        this.inMemoryUserDetails.createUser(newUser);
        return new Response(StatusCode.SUCCESS, "Add Success", newUser);
    }
}