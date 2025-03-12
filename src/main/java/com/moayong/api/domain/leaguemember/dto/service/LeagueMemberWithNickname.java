package com.moayong.api.domain.leaguemember.dto.service;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;

public record LeagueMemberWithNickname (
    LeagueMember leagueMember,
    String nickname
) {

}