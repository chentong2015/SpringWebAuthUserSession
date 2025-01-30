package backend.config;

import backend.config.handler.AuthEntryPointHandler;
import backend.config.handler.AuthLoginFailureHandler;
import backend.config.handler.AuthLoginSuccessHandler;
import backend.config.handler.MyLogoutSuccessHandler;
import backend.config.filter.AuthTokenFilter;
import backend.cookie_session.CookieManager;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthSecurityConfig {

    @Autowired
    private AuthTokenFilter authenticationTokenFilter;

    @Autowired
    private AuthEntryPointHandler authEntryPointHandler;

    @Autowired
    private AuthLoginSuccessHandler loginSuccessHandler;

    @Autowired
    private AuthLoginFailureHandler loginFailureHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))

                // 授权异常的处理器(处理401 Unauthorized请求)
                .exceptionHandling(exceptionHandler ->
                        exceptionHandler.authenticationEntryPoint(authEntryPointHandler))

                // 配置请求的过滤器，基于请求的Token来验证用户
                .addFilterBefore(authenticationTokenFilter, BasicAuthenticationFilter.class)

                // TODO. 除非是授权访问路径，否则必须通过AuthFilter来进行Authentication
                .authorizeHttpRequests((requests) -> {
                    requests.requestMatchers("/api/public").permitAll();
                    requests.requestMatchers("/api/register").permitAll();
                    requests.anyRequest().authenticated();
                })

                // 配置Login登录成功处理器，生成Token并添加到返回的Cookie中
                .formLogin(form -> {
                    form.loginPage("/api/login").permitAll();
                    form.successHandler(loginSuccessHandler);
                    form.failureHandler(loginFailureHandler);
                })

                // 配置Logout登出后处理器，清除用户的Cookie Token
                .logout(logout -> {
                     logout.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout")).permitAll();
                     logout.logoutSuccessHandler(myLogoutSuccessHandler);
                     logout.invalidateHttpSession(true);
                     logout.deleteCookies(CookieManager.getAuthCookieName());
                     logout.deleteCookies("JSESSIONID");
                });
        return httpSecurity.build();
    }
}
