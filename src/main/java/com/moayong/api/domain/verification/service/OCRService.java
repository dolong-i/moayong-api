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

            Mat mat = converterToMat.convert(converterToFrame.convert(image));
            Mat resizedMat = new Mat();
            opencv_imgproc.resize(mat, resizedMat, new Size(mat.cols() * 2, mat.rows() * 2));
            return converterToFrame.convert(converterToMat.convert(resizedMat));
        }
    }
}