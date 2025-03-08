package com.moayong.api.domain.leaguemember.dto.service;

import com.moayong.api.domain.leaguemember.dto.response.MatchInfoResponse;
import lombok.Builder;

@Builder
public record MatchInfoServiceDto(
        Long userId,
        LeagueMemberServiceDto prevMemberInfo,
        LeagueMemberServiceDto nextMemberInfo
){
    public MatchInfoResponse toResponse() {
        return MatchInfoResponse.builder()
                .userId(userId)
                .prev(prevMemberInfo.toResponse())
                .next(nextMemberInfo.toResponse())
                .build();
    }
}
