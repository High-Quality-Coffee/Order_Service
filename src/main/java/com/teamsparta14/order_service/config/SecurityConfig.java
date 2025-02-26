package com.teamsparta14.order_service.config;

import com.teamsparta14.order_service.global.enums.Role;
import com.teamsparta14.order_service.user.jwt.CustomLogoutFilter;
import com.teamsparta14.order_service.user.jwt.JWTFilter;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import com.teamsparta14.order_service.user.jwt.LoginFilter;
import com.teamsparta14.order_service.user.repository.RefreshRepository;
import com.teamsparta14.order_service.user.service.TokenReissueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final TokenReissueService tokenReissueService;
    private final RefreshRepository refreshRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Custom LoginFilter 등록
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, tokenReissueService);
        loginFilter.setFilterProcessesUrl("/api/auth/login"); // 엔드포인트를 /api/login으로 변경

        http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();
                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);

                                configuration.setExposedHeaders(Arrays.asList("Authorization", "access"));
                                return configuration;
                            }

                        }));

        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (POST 요청 허용)
                .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/join/**", "/api/auth/login", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()  // 회원가입은 인증 없이 가능
                            .requestMatchers("/api/address/**", "/api/auth/delete").hasRole("USER")
                            .requestMatchers("/api/user/list/{username}").hasRole("MASTER")
                            .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/products/search").permitAll()
                            .requestMatchers("/api/products/**").hasAnyRole("OWNER", "MASTER")
                            .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                            .requestMatchers("/api/reviews/**").hasRole("USER")
                            .requestMatchers(HttpMethod.DELETE,"/api/orders/**").hasAnyRole("MASTER","USER")
                            .requestMatchers(HttpMethod.GET,"/api/orders/{store_id}/orders").hasAnyRole("OWNER","MASTER")
                            .requestMatchers("/api/orders/**").hasAnyRole("USER","MASTER")
                            .requestMatchers("/api/payments/**").hasAnyRole("USER","MASTER")
                            .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
                )
                .formLogin(login -> login.disable())  // 기본 로그인 폼 비활성화
                .httpBasic(basic -> basic.disable()); // HTTP Basic 인증 비활성화

        http
                .logout((auth) -> auth.disable()); // 기본 로그아웃 필터 비활성화

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, tokenReissueService), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}