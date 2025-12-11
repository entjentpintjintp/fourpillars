package com.kolloseum.fourpillars.infrastructure.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolloseum.fourpillars.common.logger.ErrorLogger;
import com.kolloseum.fourpillars.common.utils.TimeUtils;
import com.kolloseum.fourpillars.infrastructure.security.filter.RateLimitFilter;
import com.kolloseum.fourpillars.infrastructure.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final RateLimitFilter rateLimitFilter;
        private final ErrorLogger errorLogger;
        private final ObjectMapper objectMapper;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring().requestMatchers("/actuator/**");
        }

        // 1. Admin Web Filter Chain (OAuth2 Login + Form Login)
        @Bean
        @Order(1)
        public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
                return http
                                .securityMatcher("/admin/**", "/login/oauth2/**", "/oauth2/**")
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/admin/login", "/login/oauth2/**", "/oauth2/**",
                                                                "/admin/css/**", "/admin/js/**", "/admin/images/**",
                                                                "/admin/monitor/assets/**",
                                                                "/admin/monitor/instances/**", "/actuator/**",
                                                                "/favicon.ico", "/error")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/admin/login")
                                                .defaultSuccessUrl("/admin/totp", true))
                                .logout(logout -> logout
                                                .logoutUrl("/admin/logout")
                                                .logoutSuccessUrl("/admin/login?logout")
                                                .permitAll())
                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/admin/login?error"))
                                .build();
        }

        // 2. API Filter Chain (JWT)
        @Bean
        @Order(2)
        public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
                return http
                                .securityMatcher("/**") // 나머지 모든 요청
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .formLogin(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/authn/login", "/authn/refresh").permitAll()
                                                .requestMatchers("/fourpillars/**").permitAll() // Public terms
                                                                                                // endpoints
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**",
                                                                "/favicon.ico", "/error")
                                                .permitAll() // Static Resources

                                                .requestMatchers("/authn/logout").authenticated()
                                                .requestMatchers("/authz/**", "/users/**", "/contents/**")
                                                .authenticated()

                                                .anyRequest().authenticated())

                                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(createAuthenticationEntryPoint())
                                                .accessDeniedHandler(createAccessDeniedHandler()))

                                .build();
        }

        private AuthenticationEntryPoint createAuthenticationEntryPoint() {
                return (request, response, authException) -> {
                        String currentTime = TimeUtils.getCurrentTimeFormatted();

                        errorLogger.logError("UNAUTHORIZED", currentTime,
                                        "Authentication failed: " + authException.getMessage());

                        response.setStatus(401);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setCharacterEncoding("UTF-8");

                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("code", "UNAUTHORIZED");
                        responseData.put("time", currentTime);
                        responseData.put("data", null);

                        response.getWriter().write(objectMapper.writeValueAsString(responseData));
                };
        }

        private AccessDeniedHandler createAccessDeniedHandler() {
                return (request, response, accessDeniedException) -> {
                        String currentTime = TimeUtils.getCurrentTimeFormatted();

                        errorLogger.logError("FORBIDDEN", currentTime,
                                        "Access denied: " + accessDeniedException.getMessage());

                        response.setStatus(403);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setCharacterEncoding("UTF-8");

                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("code", "FORBIDDEN");
                        responseData.put("time", currentTime);
                        responseData.put("data", null);

                        response.getWriter().write(objectMapper.writeValueAsString(responseData));
                };
        }
}