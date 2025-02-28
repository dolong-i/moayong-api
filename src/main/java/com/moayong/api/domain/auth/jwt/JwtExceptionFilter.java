package com.moayong.api.domain.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moayong.api.domain.auth.exception.AuthException;
import com.moayong.api.global.api.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 정상적인 필터 체인 실행
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            String logMessage = String.format("Auth exception: %s", e.getMessage());
            if (e.getErrorData() != null && !e.getErrorData().isEmpty()) {
                logMessage += ", data: " + e.getErrorData();
            }
            log.info(logMessage, e);

            response.setStatus(e.getStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ApiResponse<Object> apiResponse = ApiResponse.error("AUTH_" + e.getCode(), e.getErrorData(), e.getMessage());

            objectMapper.writeValue(response.getOutputStream(), apiResponse);
        } catch (Exception e) {
            log.error("Unexpected exception: {}", e.getMessage(), e);

            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ApiResponse<Object> apiResponse = ApiResponse.error("UNKNOWN_ERROR", null, "알 수 없는 오류 발생");

            objectMapper.writeValue(response.getOutputStream(), apiResponse);
        }
    }
}