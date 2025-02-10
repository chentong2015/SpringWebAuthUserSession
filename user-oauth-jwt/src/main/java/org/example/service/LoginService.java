package org.example.service;

import org.example.util.JwtGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private final JwtGenerator jwtProvider;
    private final InMemoryCacheService cacheService;

    public LoginService(JwtGenerator jwtProvider, InMemoryCacheService cacheService) {
        this.jwtProvider = jwtProvider;
        this.cacheService = cacheService;
    }

    // TODO. 将身份验证成功的User和它对应的Token持久化
    // 使用持久化的Token来验证用户的访问，确认请求的
    public Map<String, Object> createLoginInfo(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Jwt jwtObject = this.jwtProvider.createJwt(authentication);
        this.cacheService.setToken(userDetails.getUsername(), jwtObject.getTokenValue());

        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDetails);
        loginResultMap.put("token", jwtObject.getTokenValue());
        return loginResultMap;
    }
}
