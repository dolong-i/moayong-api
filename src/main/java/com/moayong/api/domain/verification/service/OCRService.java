package com.moayong.api.domain.verification.service;

import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class OCRService {
    private final Tesseract tesseract;

    public String extractTextFromImage(byte[] fileBytes) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(fileBytes));
            BufferedImage preProcessedImage = preprocessImage(image);
            return tesseract.doOCR(preProcessedImage);
        } catch (Exception e) {
            throw new RuntimeException("OCR 처리 중 오류 발생", e);
        }
    }

    public BufferedImage preprocessImage(BufferedImage image) {
        try (OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
             Java2DFrameConverter converterToFrame = new Java2DFrameConverter()) {

            // BufferedImage -> Mat 변환
            Mat mat = converterToMat.convert(converterToFrame.convert(image));

            // 1. 이미지 확대 (1.5배)
            Mat resizedMat = new Mat();
            opencv_imgproc.resize(mat, resizedMat, new Size((int) (mat.cols() * 1.5), (int) (mat.rows() * 1.5)), 0, 0, opencv_imgproc.INTER_LINEAR);

            // 2. 그레이스케일 변환
            Mat grayMat = new Mat();
            opencv_imgproc.cvtColor(resizedMat, grayMat, opencv_imgproc.COLOR_BGR2GRAY);

            // 3. Gaussian Blur 적용 (노이즈 감소)
            Mat blurredMat = new Mat();
            opencv_imgproc.GaussianBlur(grayMat, blurredMat, new Size(5, 5), 0);

            // 4. Otsu's Thresholding 적용 (배경 제거)
            Mat binaryMat = new Mat();
            opencv_imgproc.threshold(blurredMat, binaryMat, 0, 255, opencv_imgproc.THRESH_BINARY + opencv_imgproc.THRESH_OTSU);

            // 5. Morphological Operations (Dilation & Erosion) - 문자를 더 선명하게
            Mat kernel = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT, new Size(3, 3));
            opencv_imgproc.morphologyEx(binaryMat, binaryMat, opencv_imgproc.MORPH_CLOSE, kernel);

            // Mat -> BufferedImage 변환 후 반환
            return converterToFrame.convert(converterToMat.convert(binaryMat));
        }
    }
}