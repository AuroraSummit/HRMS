package com.hrms.config;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioConfig minioConfig;

    private MinioClient getClient() {
        return MinioClient.builder()
                .endpoint(minioConfig.getEndpoint())
                .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                .build();
    }

    public String upload(MultipartFile file) {
        try {
            MinioClient client = getClient();
            String bucket = minioConfig.getBucket();

            boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            String originalFilename = file.getOriginalFilename();
            String suffix = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String objectName = UUID.randomUUID().toString().replace("-", "") + suffix;

            try (InputStream inputStream = file.getInputStream()) {
                client.putObject(PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
            }

            return minioConfig.getEndpoint() + "/" + bucket + "/" + objectName;
        } catch (Exception e) {
            log.error("MinIO上传失败", e);
            throw new RuntimeException("文件上传失败");
        }
    }

    public String getPresignedUrl(String objectName, int expiresInMinutes) {
        try {
            MinioClient client = getClient();
            return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(objectName)
                    .method(Method.GET)
                    .expiry(expiresInMinutes, TimeUnit.MINUTES)
                    .build());
        } catch (Exception e) {
            log.error("获取文件预览URL失败", e);
            throw new RuntimeException("获取文件预览URL失败");
        }
    }

    public void delete(String objectName) {
        try {
            MinioClient client = getClient();
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("MinIO删除文件失败", e);
            throw new RuntimeException("文件删除失败");
        }
    }
}
