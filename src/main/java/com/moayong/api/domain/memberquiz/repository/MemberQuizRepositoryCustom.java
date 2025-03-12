package com.moayong.api.domain.memberquiz.repository;

import java.util.List;

public interface MemberQuizRepositoryCustom {
    List<Long> findSolvedQuizzesByUserId(Long userId);
}
