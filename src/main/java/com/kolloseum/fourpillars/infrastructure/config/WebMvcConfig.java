package com.kolloseum.fourpillars.infrastructure.config;

import com.kolloseum.fourpillars.infrastructure.security.interceptor.AdminTotpInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminTotpInterceptor())
                .addPathPatterns("/admin/**") // 모든 Admin 경로 차단
                .excludePathPatterns(
                        "/admin/login", // 로그인 페이지 제외
                        "/admin/totp/**", // TOTP 페이지 및 검증 API 제외
                        "/admin/css/**", // 정적 리소스 제외
                        "/admin/js/**",
                        "/admin/images/**",
                        "/admin/favicon.ico");
    }
}
