package org.example.config;

import org.example.service.DBUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AuthSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DBUserDetailsService dbUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests.anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())

                // TODO. 有效期内发送请求，Token会自动刷新新值和过期时间，保证持续访问
                // 1. 设置获取UserDetails的服务
                // 2. 设置Token持久化的Repository
                // 3. 设置Cookie/Token有效时间, 超过后必须重新登录
                .rememberMe(rememberMeConfigurer -> {
                    rememberMeConfigurer.userDetailsService(dbUserDetailsService);
                    rememberMeConfigurer.tokenRepository(persistentTokenRepository());
                    rememberMeConfigurer.tokenValiditySeconds(300);
                });
        return httpSecurity.build();
    }

    // TODO. Remember-Me Token数据的持久化实现
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 自动初始化DB Schema数据表
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
}

