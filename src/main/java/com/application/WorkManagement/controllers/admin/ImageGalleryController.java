package com.application.WorkManagement.controllers.admin;

import com.application.WorkManagement.dto.responses.admin.ImageGalleryResponse;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.exceptions.custom.EmptyImageException;
import com.application.WorkManagement.exceptions.custom.InvalidFileExtensionException;
import com.application.WorkManagement.services.Interface.ImageGalleryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/gallery")
@SecurityRequirement(
        name = "JWT-BEARER"
)
public class ImageGalleryController {

    private final ImageGalleryService imageGalleryService;

    public ImageGalleryController(ImageGalleryService imageGalleryService) {
        this.imageGalleryService = imageGalleryService;
    }


    @PostMapping
    public ResponseEntity<ImageGalleryResponse> saveImageIntoGallery(
            @RequestParam("image") MultipartFile file
    ) throws InvalidFileExtensionException, URISyntaxException, IOException, EmptyImageException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        imageGalleryService.saveImageIntoGallery(
                                file
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<ImageGalleryResponse>> readImagesInGallery(){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        imageGalleryService.readImagesInGallery()
                );
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ImageGalleryResponse> readImageInGallery(
            @PathVariable("imageId") UUID imageId
    ) throws DataNotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        imageGalleryService.readImageInGallery(imageId)
                );
    }

    @PatchMapping("/{imageId}")
    public ResponseEntity<ImageGalleryResponse> updateImageInGallery(
            @PathVariable("imageId") UUID imageId,
            @RequestParam("image") MultipartFile file
    ) throws DataNotFoundException, InvalidFileExtensionException, URISyntaxException, IOException, EmptyImageException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        imageGalleryService.updateImageInGallery(imageId, file)
                );
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImageInGallery(
            @PathVariable("imageId") UUID imageId
    ){
        imageGalleryService.deleteImageInGallery(imageId);
        return ResponseEntity
                .noContent()
                .build();
    }

}
