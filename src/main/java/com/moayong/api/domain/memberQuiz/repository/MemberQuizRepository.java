package com.moayong.api.domain.memberQuiz.repository;

import com.moayong.api.domain.memberQuiz.domain.MemberQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long>, MemberQuizRepositoryCustom {
}
