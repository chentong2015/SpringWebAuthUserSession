package org.example.config;

import org.example.config.auth.UserRequestAuthManager;
import org.example.config.handler.CustomBasicAuthEntryPoint;
import org.example.config.handler.CustomTokenAccessDeniedHandler;
import org.example.config.handler.CustomTokenAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthSecurityConfig {

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    @Autowired
    private UserRequestAuthManager requestAuthManager;

    @Autowired
    private CustomBasicAuthEntryPoint customBasicAuthEntryPoint;

    @Autowired
    private CustomTokenAuthEntryPoint customTokenAuthEntryPoint;

    @Autowired
    private CustomTokenAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // TODO. 定义Request请求的HttpMethod类型
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, baseUrl + "/public").permitAll()
                        .requestMatchers(HttpMethod.POST, baseUrl + "/users/register").permitAll()
                        // Admin用户具有全部操作权限
                        .requestMatchers(HttpMethod.GET, baseUrl + "/users").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, baseUrl + "/users").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE,baseUrl + "/users/**").hasAuthority("ROLE_ADMIN")
                        // User用户访问权限控制
                        .requestMatchers(HttpMethod.GET, baseUrl + "/users/**").access(requestAuthManager)
                        .requestMatchers(HttpMethod.PUT, baseUrl + "/users/**").access(requestAuthManager)
                        .requestMatchers(HttpMethod.PATCH, baseUrl + "/users/**").access(requestAuthManager)
                        // 设置其它路径匹配规则
                        .requestMatchers(EndpointRequest.to("/health", "/info")).permitAll()
                        .requestMatchers(EndpointRequest.toAnyEndpoint().excluding("extra")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/console")).permitAll()
                        .anyRequest().authenticated()
                )
                // 使用httpBasic表单提交的方式验证用户
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.customBasicAuthEntryPoint))

                // 关于OAuth2解析器相关设置
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(this.customTokenAuthEntryPoint)
                        .accessDeniedHandler(this.accessDeniedHandler));
        return httpSecurity.build();
    }
}

