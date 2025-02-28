package com.moayong.api.domain.user.controller;

import com.moayong.api.domain.auth.security.UserPrincipal;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.dto.response.UserResponse;
import com.moayong.api.domain.user.service.UserService;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("users/{id}")
    public ApiResponse<UserResponse> findUserById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        UserResponse response = new UserResponse(user);

        return ApiResponse.success(response, "유저 단건 조회 성공");
    }

    @GetMapping("/users/me")
    public ApiResponse<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userService.findUserById(userPrincipal.getUserId());
        UserResponse response = new UserResponse(user);
        return ApiResponse.success(response, "유저 조회 성공");
    }

    @GetMapping("/users")
    public ApiResponse<List<UserResponse>> findAllUsers() {
        List<User> users = userService.findAllUsers();
        List<UserResponse> response = users.stream().map(UserResponse::new).toList();

        return ApiResponse.success(response, "유저 전체 조회 성공");
    }
}
