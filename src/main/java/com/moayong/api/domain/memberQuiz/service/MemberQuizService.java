package com.moayong.api.domain.memberQuiz.service;

import com.moayong.api.domain.memberQuiz.domain.MemberQuiz;
import com.moayong.api.domain.memberQuiz.enums.MemberQuizStatus;
import com.moayong.api.domain.memberQuiz.repository.MemberQuizRepository;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.enums.QuizErrorCode;
import com.moayong.api.domain.quiz.exception.QuizException;
import com.moayong.api.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class MemberQuizService {
    private final MemberQuizRepository memberQuizRepository;
    private final QuizService quizService;

    public MemberQuiz save(MemberQuiz memberQuiz) {
        return memberQuizRepository.save(memberQuiz);
    }

    // TODO 하루 5개 제한 추가하기 - 다 풀었으면 지난 퀴즈 목록 제공
    // TODO 매일 오전 9시 공개, 퀴즈 풀기 전까지 24시간 동안 동일 퀴즈 확인해야함
    public Quiz findRandomQuiz(Long userId) {
        // 푼 퀴즈 아이디들 가져오기
        List<Long> solvedQuizIds = memberQuizRepository.findSolvedQuizIds(userId);

        // TODO 문제를 다 풀었는지 여부를 저장할 예정
        List<Quiz> quizzes = memberQuizRepository.findUnsolvedQuizzes(solvedQuizIds);
        if (quizzes.isEmpty()) {
            quizzes = quizService.findAllQuizzes();
            if (quizzes.isEmpty()) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("id", userId);
                throw new QuizException(QuizErrorCode.QUIZ_NOT_FOUND, errorData);
            }
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(quizzes.size());
        return quizzes.get(randomIndex);
    }

    public List<Quiz> findAllSolvedQuizzes(Long userId) {
        return memberQuizRepository.findAllSolvedQuizzes(userId);
    }

    public List<Quiz> findSolvedQuizzesByUserAndSeason(Long userId, Long seasonId) {
        return memberQuizRepository.findSolvedQuizzesByUserAndSeason(userId, seasonId);
    }

    public Quiz submitAnswer(Long userId, Long quizId, Integer userAnswer) {
        boolean isRightAnswer = checkAnswer(quizId, userAnswer);

        // TODO 리그멤버서비스 생성. leagueMemberService.findLeagueMemberIdByUserId(userId);
        Long leagueMemberId = 0L;

        MemberQuizStatus status = isRightAnswer ? MemberQuizStatus.SUCCESS : MemberQuizStatus.FAIL;
        MemberQuiz memberQuiz = memberQuizRepository.save(
                MemberQuiz.builder()
                        .leagueMemberId(leagueMemberId)
                        .quizId(quizId)
                        .status(status)
                        .build()
        );

        /**
         * TODO Redis 처리 - total_score
         */

        return quizService.findById(quizId);
    }

    private boolean checkAnswer(Long quizId, Integer userAnswer) {
        Quiz quiz = quizService.findById(quizId);
        return quiz.getAnswerNumber().equals(userAnswer);
    }
}
