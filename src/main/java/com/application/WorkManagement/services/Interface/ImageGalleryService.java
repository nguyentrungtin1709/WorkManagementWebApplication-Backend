package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.responses.admin.ImageGalleryResponse;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.exceptions.custom.EmptyImageException;
import com.application.WorkManagement.exceptions.custom.InvalidFileExtensionException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

public interface ImageGalleryService {
    ImageGalleryResponse saveImageIntoGallery(MultipartFile file) throws InvalidFileExtensionException, URISyntaxException, IOException, EmptyImageException;

    List<ImageGalleryResponse> readImagesInGallery();

    ImageGalleryResponse readImageInGallery(UUID imageId) throws DataNotFoundException;

    ImageGalleryResponse updateImageInGallery(UUID imageId, MultipartFile file) throws DataNotFoundException, URISyntaxException, InvalidFileExtensionException, IOException, EmptyImageException;

    void deleteImageInGallery(UUID imageId);

}
