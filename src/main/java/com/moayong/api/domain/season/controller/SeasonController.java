package com.moayong.api.domain.season.controller;

import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.dto.request.SeasonSaveRequest;
import com.moayong.api.domain.season.dto.response.SeasonResponse;
import com.moayong.api.domain.season.service.SeasonService;
import com.moayong.api.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SeasonController {
    private final SeasonService seasonService;

    @PostMapping("/api/v1/seasons")
    public ApiResponse<SeasonResponse> saveSeason(@RequestBody @Valid SeasonSaveRequest request) {
        Season season = seasonService.save(request.toEntity());
        SeasonResponse response =  new SeasonResponse(season);

        return ApiResponse.success(response, "시즌 등록 성공");
    }
}
