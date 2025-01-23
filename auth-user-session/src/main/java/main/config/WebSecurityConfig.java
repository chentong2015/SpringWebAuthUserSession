package main.config;

import main.backend.authentication.filter.TokenAuthenticationFilter;
import main.backend.authentication.handler.AuthenticationEntryPointHandler;
import main.backend.authentication.handler.AuthenticationFailureHandler;
import main.backend.authentication.handler.AuthenticationSuccessHandler;
import main.config.handler.MyLogoutSuccessHandler;
import main.backend.service.PersistenceUserDetailsService;
import main.backend.session.CookieHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthenticationEntryPointHandler authEntryPointHandler;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    private AuthenticationSuccessHandler authSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authFailureHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private PersistenceUserDetailsService persistenceUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandler ->
                        exceptionHandler.authenticationEntryPoint(authEntryPointHandler) )
                // 配置每一个请求过滤器，基于请求的Token来验证用户
                .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class)
                // 配置需要认证的特定请求路径
                .authorizeHttpRequests((requests) -> {
                    // 针对前端Page路径的配置
                    requests.requestMatchers("/login").permitAll();
                    requests.requestMatchers("/register").permitAll();
                    requests.requestMatchers("/public").permitAll();
                    requests.requestMatchers("/api/signup").permitAll();
                    requests.anyRequest().authenticated();
                })
                // 配置Login登录成功处理器，生成Token并添加到返回的Cookie中
                .formLogin(form -> {
                    form.loginPage("/api/login").permitAll();
                    form.successHandler(authSuccessHandler);
                    form.failureHandler(authFailureHandler);
                })
                // 配置Logout登出后处理器，并且清除用户的Cookie Token
                .logout(logout -> {
                     logout.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout")).permitAll();
                     logout.logoutSuccessHandler(myLogoutSuccessHandler);
                     logout.invalidateHttpSession(true);
                     logout.deleteCookies(CookieHandler.getAuthCookieName());
                     logout.deleteCookies("JSESSIONID");
                });
        return httpSecurity.build();
    }

    // 设置AuthenticationManager: 使用特定的UserDetails + PasswordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(persistenceUserDetailsService);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder globalPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
