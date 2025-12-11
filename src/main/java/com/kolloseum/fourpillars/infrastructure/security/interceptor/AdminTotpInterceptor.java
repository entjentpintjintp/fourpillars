package com.kolloseum.fourpillars.infrastructure.security.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AdminTotpInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 이미 인증된 세션인지 확인
        HttpSession session = request.getSession();
        Object verified = session.getAttribute("TOTP_VERIFIED");

        if (verified != null && (Boolean) verified) {
            return true; // 통과
        }

        // 인증되지 않음 -> 리다이렉트
        log.warn("Access denied: TOTP verification required for {}", request.getRequestURI());
        response.sendRedirect("/admin/totp");
        return false;
    }
}
