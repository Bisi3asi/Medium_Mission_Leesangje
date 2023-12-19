package com.example.medium.global.security;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS)) // 세션 생성 비활성화
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/**").permitAll()
                )
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 비활성화(JSESSIONID 생성 억제)
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화(JSESSIONID 생성 억제)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}