package org.isuncy.wtet_backend.config;

import org.isuncy.wtet_backend.interceptor.LoginCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheck loginCheck;      //注入拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheck)
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/doc.html");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
