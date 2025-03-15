package com.moayong.api.domain.verification.config;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TesseractConfig {

    private static final String TESSDATA_PATH_MAC = "/opt/homebrew/share/tessdata";
    private static final String TESSDATA_PATH_LINUX = "/usr/share/tesseract-ocr/5/tessdata";
    private static final String TESSDATA_PATH_WINDOWS = "C:\\Program Files\\Tesseract-OCR\\tessdata";
    private static final String LIBRARY_PATH_MAC = "/opt/homebrew/lib";

    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        String os = System.getProperty("os.name").toLowerCase();

        tesseract.setDatapath(getTessDatapath(os));
        tesseract.setLanguage("kor");  // 한글 OCR 지원
        tesseract.setPageSegMode(6);  // 문서 자동 분석

        // 네이티브 라이브러리 경로 설정
        if (os.contains("mac")) {
            System.setProperty("jna.library.path", LIBRARY_PATH_MAC);
            System.setProperty("java.library.path", LIBRARY_PATH_MAC);
        }

        return tesseract;
    }

    private String getTessDatapath(String os) {
        if (os.contains("mac")) return TESSDATA_PATH_MAC;
        if (os.contains("linux")) return TESSDATA_PATH_LINUX;
        if (os.contains("win")) return TESSDATA_PATH_WINDOWS;
        throw new IllegalStateException("Unsupported OS: " + os);
    }
}