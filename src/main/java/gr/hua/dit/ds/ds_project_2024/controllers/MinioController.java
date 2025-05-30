package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.service.MinioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class MinioController {

    @Autowired
    private MinioService minioService;

    @PostConstruct
    public ResponseEntity<String> run() {
        String[] filenames = {"dog1.jpg", "dog2.jpg", "dog3.jpg", "dog4.jpg"};

        for (String filename : filenames) {
            try (InputStream is = new ClassPathResource("static/images/" + filename).getInputStream()) {
                minioService.uploadFile(filename, is, "image/jpeg");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload " + filename + ": " + e.getMessage());
            }
        }
        return ResponseEntity.ok("All images uploaded successfully.");
    }

    @RequestMapping("/upload")
    public void uploadFile(MultipartFile file) {
        try {
            minioService.uploadFile(
                    file.getOriginalFilename(),
                    file.getInputStream(),
                    file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload " + file.getOriginalFilename() + ": " + e.getMessage());
        }
    }
}
