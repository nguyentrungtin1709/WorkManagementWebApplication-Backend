package com.application.WorkManagement.controllers;

import com.application.WorkManagement.dto.responses.admin.ImageGalleryResponse;
import com.application.WorkManagement.services.Interface.ImageGalleryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
public class ImageGalleryController {

    private final ImageGalleryService imageGalleryService;

    public ImageGalleryController(ImageGalleryService imageGalleryService) {
        this.imageGalleryService = imageGalleryService;
    }

    @GetMapping
    public ResponseEntity<List<ImageGalleryResponse>> readImages(){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        imageGalleryService.readImagesInGallery()
                );
    }
    
}
