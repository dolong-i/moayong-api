package com.moayong.api.domain.savings.service;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.service.LeagueMemberService;
import com.moayong.api.domain.savings.domain.Savings;
import com.moayong.api.domain.savings.dto.service.SavingsServiceDto;
import com.moayong.api.domain.savings.enums.SavingsErrorCode;
import com.moayong.api.domain.savings.enums.SavingsPolicy;
import com.moayong.api.domain.savings.exception.SavingsException;
import com.moayong.api.domain.savings.repository.SavingsRepository;
import com.moayong.api.domain.season.service.SeasonService;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.service.UserService;
import com.moayong.api.domain.verification.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SavingsService {

    private final SavingsRepository savingsRepository;
    private final LeagueMemberService memberService;
    private final UserService userService;
    private final SeasonService seasonService;

    @Transactional
    public Savings saveSavings(Long memberId, SavingsServiceDto serviceDto) {
        LeagueMember member = memberService.findById(memberId);

        validateSavings(member, serviceDto);
        Integer score = getScore(member, serviceDto);
        memberService.addScore(memberId, score);

        Savings savings = Savings.builder()
                .leagueMemberId(member.getId())
                .imageUrl(serviceDto.imageUrl())
                .score(score)
                .amount(serviceDto.amount())
                .datetime(serviceDto.datetime())
                .build();

        return savingsRepository.save(savings);
    }

    public List<Savings> findSavingsByMemberId(Long memberId) {
        return savingsRepository.findByLeagueMemberId(memberId);
    }

    public List<Savings> findSavingsByUserId(Long userId) {
        return savingsRepository.findByUserId(userId);
    }

    public Savings findSavingsById(Long savingsId) {
        return savingsRepository.findById(savingsId)
                .orElseThrow(() -> new SavingsException(SavingsErrorCode.SAVINGS_NOT_FOUND));
    }

    private void validateSavings(LeagueMember member, SavingsServiceDto serviceDto) {
        User user = userService.findUserById(member.getUserId());
        if (serviceDto.type() == TransactionType.WITHDRAW) {
            throw new SavingsException(SavingsErrorCode.WITHDRAWAL_NOT_ALLOWED);
        }

        if (!Objects.equals(serviceDto.name(), user.getName())) {
            throw new SavingsException(SavingsErrorCode.USER_NAME_MISMATCH);
        }

        LocalDateTime seasonStartedAt = seasonService.findOpenSeason().getStartedAt();
        LocalDateTime savingsAt = serviceDto.datetime();
        if (!savingsAt.isAfter(seasonStartedAt)) {
            throw new SavingsException(SavingsErrorCode.NOT_WITHIN_CURRENT_SEASON);
        }

        Integer totalAmount = findSavingsTotalAmountByUserId(member.getUserId());
        Integer balance = serviceDto.balance() - serviceDto.amount();
        if (!Objects.equals(totalAmount, balance)) {
            throw new SavingsException(SavingsErrorCode.BALANCE_MISMATCH_BEFORE_SAVINGS);
        }
    }

    private Integer getScore(LeagueMember member, SavingsServiceDto serviceDto) {
        Integer totalScore = member.getTotalScore();
        Integer nowScore = findSavingsTotalScoreByMemberId(member.getId());
        if (nowScore >= totalScore) {
            return 0;
        }

        Integer maxScore = SavingsPolicy.MAX_SCORE.getValue();
        Integer goalAmount = member.getGoalAmount();
        Integer savingsScore = goalAmount / serviceDto.amount() * maxScore;

        if (nowScore + savingsScore >= maxScore) {
            return maxScore - nowScore;
        } else {
            return savingsScore;
        }
    }

    public Integer findSavingsTotalAmountByMemberId(Long memberId) {
        return savingsRepository.findSavingsTotalAmountByLeagueMemberId(memberId);
    }

    public Integer findSavingsTotalAmountByUserId(Long userId) {
        return savingsRepository.findSavingsTotalAmountByUserId(userId);
    }

    private Integer findSavingsTotalScoreByMemberId(Long memberId) {
        return savingsRepository.findSavingsTotalScoreByLeagueMemberId(memberId);
    }
}