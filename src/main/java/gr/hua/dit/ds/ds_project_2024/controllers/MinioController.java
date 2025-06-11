package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.service.MinioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class MinioController {

    @Autowired
    private MinioService minioService;

    // Uploads all the pet photos of the pets we initialize in file starter_data.sql and background images
    // we use in our project in the MinIO storage before starting the app (PostConstruct)
    @PostConstruct
    public ResponseEntity<String> run() {
        String[] filenames = {"dog1.jpg", "dog2.jpg", "dog3.jpg", "dog4.jpg"};
        String[] pet_photos = {"Buddy.jpg", "Bella.jpg", "Coco.jpg", "Daisy.jpg", "Max.jpg", "Mittens.jpg", "Rocky.jpg", "Silver.jpg"};

        for (String photo : pet_photos) {
            try (InputStream is = new ClassPathResource("static/images/pet-photos" + photo).getInputStream()) {
                minioService.uploadMenuImages("/pet-photos/" + photo, is, "image/jpeg");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload " + photo + ": " + e.getMessage());
            }
        }

        for (String filename : filenames) {
            try (InputStream is = new ClassPathResource("static/images/" + filename).getInputStream()) {
                minioService.uploadMenuImages(filename, is, "image/jpeg");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload " + filename + ": " + e.getMessage());
            }
        }
        return ResponseEntity.ok("All images uploaded successfully.");
    }
}
