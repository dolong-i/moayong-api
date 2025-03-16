package com.moayong.api.domain.auth.config;

import com.moayong.api.domain.auth.jwt.JwtAuthenticationFilter;
import com.moayong.api.domain.auth.jwt.JwtExceptionFilter;
import com.moayong.api.domain.auth.oauth2.handler.OAuth2LoginFailureHandler;
import com.moayong.api.domain.auth.oauth2.handler.OAuth2LoginSuccessHandler;
import com.moayong.api.domain.auth.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final RequestLoggingFilter requestLoggingFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(requestLoggingFilter, UsernamePasswordAuthenticationFilter.class) // 맨 윗단에서 실행
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/authorize", "/login/oauth2/code/*").permitAll()
                        .requestMatchers("/api/v1/leagues/**").permitAll()
                        .requestMatchers("/api/v1/seasons/**").permitAll()
                        .requestMatchers("/api/v1/auth/refresh").permitAll()
                        .requestMatchers("/api/v1/auth/logout").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .baseUri("/api/v1/auth/authorize"))
                        .redirectionEndpoint(endpoint -> endpoint
                                .baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, OAuth2LoginAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
        return http.build();
    }
}
