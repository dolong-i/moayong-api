package com.moayong.api.domain.verification.util;

import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.domain.verification.dto.service.AccountServiceDto;
import com.moayong.api.domain.verification.dto.service.PaymentServiceDto;
import com.moayong.api.domain.verification.enums.TransactionType;
import com.moayong.api.domain.verification.enums.VerificationErrorCode;
import com.moayong.api.domain.verification.exception.VerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class OCRTextExtractor {
    public AccountServiceDto extractAccount(SavingsBank bank, String text) {
        return switch (bank) {
            case HANA -> extractHanaBankAccount(text);
            default -> throw new VerificationException(VerificationErrorCode.UNSUPPORTED_BANK);
        };
    }

    public PaymentServiceDto extractPayment(SavingsBank bank, String text) {
        return switch (bank) {
            case HANA -> extractHanaBankPayment(text);
            default -> throw new VerificationException(VerificationErrorCode.UNSUPPORTED_BANK);
        };
    }


    public AccountServiceDto extractHanaBankAccount(String text) {
        try {
            log.info("Extracting HANA bank account: {}", text);
            String[] sentences = text.split("\n");

            String accountNumber = sentences[4].trim();
            String balanceText = sentences[5].trim();

            Integer balance = getOnlyNumbersFromText(balanceText);

            return AccountServiceDto.builder()
                    .accountNumber(accountNumber)
                    .accountBalance(balance)
                    .build();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new VerificationException(VerificationErrorCode.ACCOUNT_TEXT_EXTRACT_FAILURE);
        }
    }

    public PaymentServiceDto extractHanaBankPayment(String text) {
        try {
            // 정규식 패턴 정의
            Pattern namePattern = Pattern.compile("거래내역상세.*\\n.*\\n(.*)\\n");
            Pattern datePattern = Pattern.compile("거래일시\\s+(.*)");
            Pattern amountPattern = Pattern.compile("거래금액\\s+(.*)원");
            Pattern balancePattern = Pattern.compile("거래후잔액\\s+(.*) 원");

            // 데이터 추출
            String name = extractMatch(namePattern, text);
            String dateStr = extractMatch(datePattern, text);
            String amountStr = extractMatch(amountPattern, text);
            String balanceStr = extractMatch(balancePattern, text);

            // 날짜 변환
            LocalDateTime date = null;
            if (dateStr != null) {
                dateStr = dateStr.replace("/", ""); // "20:0/7:29" 처리
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
                date = LocalDateTime.parse(dateStr, formatter);
            }

            TransactionType transactionType = Objects.requireNonNull(amountStr)
                    .contains("+")
                    ? TransactionType.DEPOSIT
                    : TransactionType.WITHDRAW;

            return PaymentServiceDto.builder()
                    .name(name)
                    .type(transactionType)
                    .date(date)
                    .amount(getOnlyNumbersFromText(amountStr))
                    .balance(getOnlyNumbersFromText(Objects.requireNonNull(balanceStr)))
                    .build();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new VerificationException(VerificationErrorCode.PAYMENT_TEXT_EXTRACT_FAILURE);
        }
    }

    private Integer getOnlyNumbersFromText(String text) {
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    private static String extractMatch(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }
}