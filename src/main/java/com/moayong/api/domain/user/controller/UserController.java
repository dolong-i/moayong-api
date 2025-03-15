package com.moayong.api.domain.user.controller;

import com.moayong.api.domain.auth.config.UserPrincipal;
import com.moayong.api.domain.auth.service.AuthService;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.dto.request.UserAccountUpdateRequest;
import com.moayong.api.domain.user.dto.request.UserUpdateRequest;
import com.moayong.api.domain.user.dto.response.UserResponse;
import com.moayong.api.domain.user.service.UserCommandService;
import com.moayong.api.domain.user.service.UserService;
import com.moayong.api.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final UserCommandService userCommandService;
    private final AuthService authService;

    @GetMapping("/users/{id}")
    public ApiResponse<UserResponse> findUserById(@PathVariable("id") Long id,
                                                  @AuthenticationPrincipal UserPrincipal principal) {
        authService.validateUserAccess(id, Long.valueOf(principal.getUserId()));
        User user = userService.findUserById(id);
        UserResponse response = new UserResponse(user);

        return ApiResponse.success(response, "유저 단건 조회 성공");
    }

    @GetMapping("/users/me")
    public ApiResponse<UserResponse> findCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        Long id = Long.valueOf(principal.getUserId());
        User user = userService.findUserById(id);
        UserResponse response = new UserResponse(user);

        return ApiResponse.success(response, "유저 조회 성공");
    }

    @GetMapping("/users")
    public ApiResponse<List<UserResponse>> findAllUsers() {
        List<User> users = userService.findAllUsers();
        List<UserResponse> response = users.stream().map(UserResponse::new).toList();

        return ApiResponse.success(response, "유저 전체 조회 성공");
    }

    @PutMapping("/users/{id}")
    public ApiResponse<UserResponse> updateUserInfo(@PathVariable("id") Long id,
                                                    @RequestBody @Valid UserUpdateRequest request,
                                                    @AuthenticationPrincipal UserPrincipal principal) {
        authService.validateUserAccess(id, Long.valueOf(principal.getUserId()));
        User user = userCommandService.updateUserInfo(id, request.toEntity());

        UserResponse response = new UserResponse(user);

        return ApiResponse.success(response, "유저 정보 수정 성공");
    }

    @PutMapping("/users/{id}/account")
    public ApiResponse<UserResponse> updateUserAccount(@PathVariable("id") Long id,
                                                       @RequestBody @Valid UserAccountUpdateRequest request,
                                                       @AuthenticationPrincipal UserPrincipal principal) {
        authService.validateUserAccess(id, Long.valueOf(principal.getUserId()));
        User user = userCommandService.updateUserAccount(id, request.toDto());
        UserResponse response = new UserResponse(user);

        return ApiResponse.success(response, "계좌 정보 수정 성공");
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal UserPrincipal principal) {
        authService.validateUserAccess(id, Long.valueOf(principal.getUserId()));
        userCommandService.deleteUser(id);

        return ApiResponse.success(null, "회원 탈퇴 성공");
    }

    @GetMapping("/users/nickname-check")
    public ApiResponse<Void> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        userService.checkDuplicateNickname(nickname);

        return ApiResponse.success(null, "사용 가능한 닉네임입니다.");
    }
}
