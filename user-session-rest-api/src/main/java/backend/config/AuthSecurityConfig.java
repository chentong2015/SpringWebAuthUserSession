package backend.config;

import backend.handler.AuthEntryPointHandler;
import backend.handler.AuthLoginFailureHandler;
import backend.handler.AuthLoginSuccessHandler;
import backend.handler.MyLogoutSuccessHandler;
import backend.session.CookieTokenHelper;
import backend.session.TokenRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthSecurityConfig {

    @Autowired
    private TokenRequestFilter authenticationTokenFilter;

    @Autowired
    private AuthEntryPointHandler authEntryPointHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(AbstractHttpConfigurer::disable)
             .csrf(AbstractHttpConfigurer::disable)

             // Client must send its credentials every time when tries to access a protected resource
             .sessionManagement(session -> {
                 session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                 // Controls the maximum number of sessions for a user. The default is to allow any number of sessions.
                 session.maximumSessions(1);
             })

             // 授权异常的处理器(处理401 Unauthorized请求)
             .exceptionHandling(exceptionHandler ->
                     exceptionHandler.authenticationEntryPoint(authEntryPointHandler))

             // 配置请求的过滤器，基于请求的Token来验证用户
             .addFilterBefore(authenticationTokenFilter, BasicAuthenticationFilter.class)

             // TODO. 除非是授权访问路径，否则必须通过AuthFilter来进行Authentication
             .authorizeHttpRequests((requests) -> {
                 requests.requestMatchers("/api/public").permitAll();
                 requests.requestMatchers("/api/register").permitAll();
                 requests.requestMatchers("/api/registration/code").permitAll();
                 requests.anyRequest().authenticated();
             })

             // 配置Login登录成功处理器，生成Token并添加到返回的Cookie中
             .formLogin(form -> {
                 // 提供登录的REST API地址，接受POST请求(传递用户名称和密码)
                 form.loginPage("/api/login").permitAll();
                 form.successHandler(new AuthLoginSuccessHandler());
                 form.failureHandler(new AuthLoginFailureHandler());
             })

             // 配置Logout登出后处理器，清除用户的Cookie Token
             .logout(logout -> {
                 // 提供登出的REST API地址，接受POST请求
                 logout.logoutUrl("/api/logout").permitAll();
                 logout.logoutSuccessHandler(new MyLogoutSuccessHandler());
                 logout.invalidateHttpSession(true);
                 logout.deleteCookies(CookieTokenHelper.getAuthCookieName());
                 logout.deleteCookies("JSESSIONID");
             });
        return httpSecurity.build();
    }
}
