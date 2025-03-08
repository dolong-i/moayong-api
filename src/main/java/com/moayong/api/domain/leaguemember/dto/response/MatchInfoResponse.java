package com.moayong.api.domain.leaguemember.dto.response;

import lombok.Builder;

@Builder
public record MatchInfoResponse (
        Long userId,
        LeagueMemberResponse prev,
        LeagueMemberResponse next
){

}
