package org.example.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 为请求添加自定义的拦截器
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebInterceptorConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.jwtInterceptor).addPathPatterns("/**");
    }
}
