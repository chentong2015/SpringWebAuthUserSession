package backend.config;

import backend.service.DBUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// TODO. 关于用户身份的验证, 验证UserDetails特定数据源
@Configuration
public class UserDetailsConfig {

    @Autowired
    private DBUserDetailsService dbUserDetailsService;

    @Bean
    public PasswordEncoder globalPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO. AuthenticationManager: 用于验证特定的UserDetails用户信息
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(dbUserDetailsService)
                .passwordEncoder(globalPasswordEncoder());
        return authenticationManagerBuilder.build();
    }
}
