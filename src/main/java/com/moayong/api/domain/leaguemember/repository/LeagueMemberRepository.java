package com.moayong.api.domain.leaguemember.repository;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueMemberRepository extends JpaRepository<LeagueMember, Long>, LeagueMemberRepositoryCustom {

    List<LeagueMember> findByLeagueId(Long leagueId);

    List<LeagueMember> findAllByUserId(Long userId);
}
