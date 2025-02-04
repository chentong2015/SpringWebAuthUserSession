package org.example.service;

import org.example.auth.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private final JwtProvider jwtProvider;
    private final InMemoryCacheService cacheService;

    public LoginService(JwtProvider jwtProvider, InMemoryCacheService cacheService) {
        this.jwtProvider = jwtProvider;
        this.cacheService = cacheService;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = this.jwtProvider.createToken(authentication);
        this.cacheService.setToken(userDetails.getUsername(), token);

        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDetails);
        loginResultMap.put("token", token);
        return loginResultMap;
    }
}
