package gr.hua.dit.ds.ds_project_2024.service;

import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public void uploadFile(String objectName, InputStream inputStream, String contentType) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(PutObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }

    public void downloadFile(String objectName, String destinationPath) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build())) {

            Files.copy(stream, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage(), e);
        }
    }

    public byte[] getFile(String objectName) {
        try (InputStream stream =
                     minioClient.getObject(GetObjectArgs
                             .builder()
                             .bucket(bucketName)
                             .object(objectName)
                             .build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving file from MinIO: " + e.getMessage());
        }
    }
}
