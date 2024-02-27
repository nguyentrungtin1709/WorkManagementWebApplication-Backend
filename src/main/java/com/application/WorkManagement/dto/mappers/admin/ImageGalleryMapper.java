package com.application.WorkManagement.dto.mappers.admin;

import com.application.WorkManagement.dto.responses.admin.ImageGalleryResponse;
import com.application.WorkManagement.entities.ImageGallery;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ImageGalleryMapper implements Function<ImageGallery, ImageGalleryResponse> {

    @Override
    public ImageGalleryResponse apply(ImageGallery imageGallery) {
        return ImageGalleryResponse
                .builder()
                .id(imageGallery.getUuid())
                .imageUri(imageGallery.getImageUri())
                .createdAt(imageGallery.getCreatedAt())
                .build();
    }

}
