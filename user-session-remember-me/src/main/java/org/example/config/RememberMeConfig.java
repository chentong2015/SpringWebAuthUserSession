package org.example.config;

import org.example.service.DBUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;

// TODO. 关于Remember-Me服务的底层实现
// 1. Filter过滤拦截请求
// 2. TokenBasedRememberMeServices autoLogin认证登录
// 3. RememberMeAuthenticationToken 返回认证后的对象
// https://docs.spring.io/spring-security/reference/servlet/authentication/rememberme.html
public class RememberMeConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DBUserDetailsService dbUserDetailsService;

    // TODO. 自动调用RememberMeServices服务的autoLogin()方法认证用户
    // Authentication autoLogin(HttpServletRequest request, HttpServletResponse response);
    // void loginFail(HttpServletRequest request, HttpServletResponse response);
    // void loginSuccess(HttpServletRequest request, HttpServletResponse response,,)
    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("myKey",
                userDetailsService, encodingAlgorithm);
        rememberMe.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
        return rememberMe;
    }

    // TODO. 请求被RememberMeAuthenticationFilter拦截
    //  自动调用rememberMeServices接口的autoLogin()方法，验证请求中的Cookies信息
    @Bean
    RememberMeAuthenticationFilter rememberMeFilter() {
        return new RememberMeAuthenticationFilter(authenticationManager, rememberMeServices());
    }

    // TODO. RememberMeServices需要从UserDetailsService中获取用户信息
    //  验证成功后返回RememberMeAuthenticationToken对象
    @Bean
    TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("springRocks", dbUserDetailsService);
    }

    @Bean
    RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider("springRocks");
    }
}
