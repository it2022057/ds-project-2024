package gr.hua.dit.ds.ds_project_2024.service;

import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    // Uploads a menu image to MinIO storage. If the bucket doesn't exist, it is created and a public read policy is applied
    public void uploadMenuImages(String objectName, InputStream inputStream, String contentType) {
        try {
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            setPublicReadPolicy(); // Ensures uploaded files are publicly readable
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

    // Uploads an image file (mostly) to a specific folder inside the bucket
    public void uploadFile(String folderName, String objectName, MultipartFile file) {
        try {
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            setPublicReadPolicy();
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(folderName + "/" + objectName)
                    .stream(file.getInputStream(), file.getInputStream().available(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload " + file.getOriginalFilename() + ": " + e.getMessage());
        }
    }

    // Deletes a file (object) from a specific folder in the bucket
    public void deleteFile(String folderName, String objectName) {
        try {
            if (bucketExists(bucketName)) {
                // List objects matching the prefix
                Iterable<Result<Item>> results = minioClient.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(bucketName)
                                .prefix(folderName + "/" + objectName)
                                .build()
                );
                String item = "";
                for (Result<Item> result : results) {
                    item = result.get().objectName();  // Get full object name
                }

                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(item)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete " + objectName + ": " + e.getMessage());
        }
    }

    // Downloads a file from MinIO and saves it to a given local path
    public void downloadFile(String objectName, String destinationPath) {
        if (bucketExists(objectName)) {
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
    }

    // Applies a public read-only policy to the bucket so files can be accessed via public URLs
    public void setPublicReadPolicy() {
        String readOnlyPolicy = "{\n" +
                "  \"Version\": \"2012-10-17\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Effect\": \"Allow\",\n" +
                "      \"Principal\": \"*\",\n" +
                "      \"Action\": [\n" +
                "        \"s3:GetObject\"\n" +
                "      ],\n" +
                "      \"Resource\": [\n" +
                "        \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
            if (bucketExists(bucketName)) {
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(readOnlyPolicy)
                        .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set public read policy on bucket: " + e.getMessage());
        }
    }

    // Gets a file from MinIO and returns its content as a byte array
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

    // Deletes a bucket by name (only if it exists)
    public void deleteBucket(String bucketName) {
        try {
            if (bucketExists(bucketName)) {
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete bucket: " + e.getMessage());
        }
    }

    // Checks whether a bucket with the specified name exists.
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
