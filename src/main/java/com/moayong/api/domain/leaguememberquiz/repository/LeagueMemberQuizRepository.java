package com.moayong.api.domain.leaguememberquiz.repository;

import com.moayong.api.domain.leaguememberquiz.domain.LeagueMemberQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueMemberQuizRepository extends JpaRepository<LeagueMemberQuiz, Long> {
}
