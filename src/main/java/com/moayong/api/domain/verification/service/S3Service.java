package com.moayong.api.domain.verification.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Getter
    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(byte[] fileBytes, String originalFilename) {
        String fileName = UUID.randomUUID() + "_" + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileBytes.length);
        metadata.setContentType("image/png");

        amazonS3.putObject(bucketName, fileName, new ByteArrayInputStream(fileBytes), metadata);

        return "https://d3t251u9x9cmf6.cloudfront.net/" + fileName;
    }
}