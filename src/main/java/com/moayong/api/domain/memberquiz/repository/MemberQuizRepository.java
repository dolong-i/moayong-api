package com.moayong.api.domain.memberquiz.repository;

import com.moayong.api.domain.memberquiz.domain.MemberQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long>, MemberQuizRepositoryCustom {
}
