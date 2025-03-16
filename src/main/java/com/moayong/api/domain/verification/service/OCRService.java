package com.moayong.api.domain.verification.service;

import com.moayong.api.domain.user.enums.SavingsBank;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OCRService {
    private final Tesseract tesseract;

    public String extractTextFromImage(byte[] fileBytes, SavingsBank bank) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(fileBytes));
            BufferedImage preProcessedImage = preprocessImage(image, bank);
            tesseract.setVariable("tessedit_char_blacklist", "/");// 해당경로에 이미지를 저장함.
            ImageIO.write(preProcessedImage, "jpg", new File("abc.jpg"));
            return tesseract.doOCR(preProcessedImage);
        } catch (Exception e) {
            throw new RuntimeException("OCR 처리 중 오류 발생", e);
        }
    }

    public BufferedImage preprocessImage(BufferedImage image, SavingsBank bank) {
        try (OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
             Java2DFrameConverter converterToFrame = new Java2DFrameConverter()) {

            // BufferedImage -> Mat 변환
            Mat mat = converterToMat.convert(converterToFrame.convert(image));

            // 1. 이미지 확대 (1.5배)
            Mat resizedMat = new Mat();
            opencv_imgproc.resize(mat, resizedMat, new Size((int) (mat.cols() * 1.5), (int) (mat.rows() * 1.5)), 0, 0, opencv_imgproc.INTER_LINEAR);

            Mat resultMat;
            if (bank.equals(SavingsBank.KAKAO_BANK)) {
                // 1. HSV 변환
                Mat hsvMat = new Mat();
                opencv_imgproc.cvtColor(resizedMat, hsvMat, opencv_imgproc.COLOR_BGR2HSV);

                // 2. 노란색 범위 설정 (최적화된 값)
                Mat lowerBound = new Mat(hsvMat.size(), hsvMat.type(), new Scalar(24, 180, 180, 0));
                Mat upperBound = new Mat(hsvMat.size(), hsvMat.type(), new Scalar(30, 255, 255, 0));

                Mat yellowMask = new Mat();
                opencv_core.inRange(hsvMat, lowerBound, upperBound, yellowMask);

                // 3. 배경을 흰색으로
                Mat whiteBackground = new Mat(resizedMat.size(), resizedMat.type(), new Scalar(255, 255, 255, 0));

                resultMat = new Mat();
                whiteBackground.copyTo(resultMat, yellowMask);
                opencv_core.bitwise_not(yellowMask, yellowMask);
                resizedMat.copyTo(resultMat, yellowMask);
            } else {
                resultMat = resizedMat.clone();
            }

            ImageIO.write(converterToFrame.convert(converterToMat.convert(resultMat)), "jpg", new File("ab.jpg"));

            // 2. 그레이스케일 변환 + Otsu's Thresholding 적용 (배경 제거)
            Mat binaryMat = new Mat();
            opencv_imgproc.cvtColor(resultMat, binaryMat, opencv_imgproc.COLOR_BGR2GRAY);
            opencv_imgproc.threshold(binaryMat, binaryMat, 0, 255, opencv_imgproc.THRESH_BINARY + opencv_imgproc.THRESH_OTSU);

            // 3. Morphological Operation (Dilation 적용하여 글씨 강조)
            Mat kernel = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT, new Size(2, 2));
            opencv_imgproc.dilate(binaryMat, binaryMat, kernel);

            // Mat -> BufferedImage 변환 후 반환
            return converterToFrame.convert(converterToMat.convert(binaryMat));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}