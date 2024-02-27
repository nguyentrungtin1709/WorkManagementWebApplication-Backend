package com.application.WorkManagement.services.admin;

import com.application.WorkManagement.dto.mappers.admin.ImageGalleryMapper;
import com.application.WorkManagement.dto.responses.admin.ImageGalleryResponse;
import com.application.WorkManagement.entities.ImageGallery;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.exceptions.custom.EmptyImageException;
import com.application.WorkManagement.exceptions.custom.InvalidFileExtensionException;
import com.application.WorkManagement.repositories.ImageGalleryRepository;
import com.application.WorkManagement.services.Interface.ImageGalleryService;
import com.application.WorkManagement.services.Interface.UploadImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageGalleryServiceImpl implements ImageGalleryService {

    private final UploadImageService uploadImageService;

    private final ImageGalleryMapper imageGalleryMapper;

    private final ImageGalleryRepository imageGalleryRepository;

    public ImageGalleryServiceImpl(
            UploadImageService uploadImageService,
            ImageGalleryMapper imageGalleryMapper,
            ImageGalleryRepository imageGalleryRepository
    ) {
        this.uploadImageService = uploadImageService;
        this.imageGalleryMapper = imageGalleryMapper;
        this.imageGalleryRepository = imageGalleryRepository;
    }

    @Override
    public ImageGalleryResponse saveImageIntoGallery(
            MultipartFile file
    ) throws InvalidFileExtensionException, URISyntaxException, IOException, EmptyImageException {
        URI imageUri = uploadImageService.uploadImageToS3(file);
        return imageGalleryMapper.apply(
                imageGalleryRepository.save(
                        ImageGallery
                                .builder()
                                .imageUri(imageUri)
                                .build()
                )
        );
    }

    @Override
    public List<ImageGalleryResponse> readImagesInGallery() {
        return imageGalleryRepository
                .findAll()
                .stream()
                .map(imageGalleryMapper)
                .collect(Collectors.toList());
    }

    @Override
    public ImageGalleryResponse readImageInGallery(UUID imageId) throws DataNotFoundException {
        return imageGalleryMapper.apply(
                imageGalleryRepository
                        .findById(imageId)
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy hình ảnh"))
        );
    }

    @Override
    public ImageGalleryResponse updateImageInGallery(
            UUID imageId,
            MultipartFile file
    ) throws DataNotFoundException, URISyntaxException, InvalidFileExtensionException, IOException, EmptyImageException {
        ImageGallery imageGallery = imageGalleryRepository
                .findById(imageId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy hình ảnh"));
        uploadImageService.removeFileFromS3(imageGallery.getImageUri());
        imageGallery.setImageUri(
                uploadImageService.uploadImageToS3(file)
        );
        return imageGalleryMapper.apply(
                imageGalleryRepository.save(imageGallery)
        );
    }

    @Override
    public void deleteImageInGallery(UUID imageId) {
        imageGalleryRepository.deleteById(imageId);
    }

}
