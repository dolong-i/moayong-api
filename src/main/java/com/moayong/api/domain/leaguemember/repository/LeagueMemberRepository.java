package com.moayong.api.domain.leaguemember.repository;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeagueMemberRepository extends JpaRepository<LeagueMember, Long>, LeagueMemberRepositoryCustom {
    List<LeagueMember> findByLeagueId(Long leagueId);
    List<LeagueMember> findAllByUserId(Long userId);

    @Modifying
    @Query("UPDATE LeagueMember lm SET lm.status = :newStatus WHERE lm.status = :currentStatus")
    void updateStatusByCurrentStatus(@Param("currentStatus") LeagueMemberStatus currentStatus,
                                     @Param("newStatus") LeagueMemberStatus newStatus);
}
