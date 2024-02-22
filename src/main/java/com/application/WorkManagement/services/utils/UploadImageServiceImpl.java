package com.application.WorkManagement.services.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.application.WorkManagement.entities.Image;
import com.application.WorkManagement.enums.ImageType;
import com.application.WorkManagement.exceptions.custom.EmptyImageException;
import com.application.WorkManagement.exceptions.custom.InvalidFileExtensionException;
import com.application.WorkManagement.repositories.ImageRepository;
import com.application.WorkManagement.services.Interface.UploadImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;

@Service
public class UploadImageServiceImpl implements UploadImageService {

    private ImageRepository imageRepository;

    private AmazonS3 s3Client;

    @Value("${cloud.s3.bucket.name}")
    private String bucketName;

    public UploadImageServiceImpl(ImageRepository imageRepository, AmazonS3 s3Client) {
        this.imageRepository = imageRepository;
        this.s3Client = s3Client;
    }

    @Override
    public URI uploadImageToS3(
            MultipartFile file
    ) throws EmptyImageException, InvalidFileExtensionException, URISyntaxException, IOException {
        if (!ImageType.PNG.toString().equals(file.getContentType())
                &&
            !ImageType.JPEG.toString().equals(file.getContentType()))
        {
            throw new InvalidFileExtensionException("Định dạng hình ảnh không hợp lệ");
        }
        if (file.isEmpty()){
            throw new EmptyImageException("Không có dữ liệu hình ảnh");
        }
        Image image = imageRepository.save(
                Image.builder()
                        .originalFileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .build()
        );
        File object = convertMultiPartFileToFile(file);
        String fileName = getFileName(image.getUuid(), file.getOriginalFilename());
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, object));
        Boolean isDeleted = object.delete();
        return s3Client.getUrl(bucketName, fileName).toURI();
    }

    @Override
    public void removeFileFromS3(URI uri) throws URISyntaxException {
        String fileName = extractFileNameFromURI(uri);
        s3Client.deleteObject(bucketName, fileName);
        UUID uuid = UUID.fromString(fileName.substring(0, fileName.lastIndexOf(".")));
        imageRepository.deleteById(uuid);
    }

    private String extractFileNameFromURI(URI uri) {
        String path = uri.getPath();
        return path
                .substring(
                        path.lastIndexOf("/") + 1
                );
    }


    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(
                Objects.requireNonNull(
                        file.getOriginalFilename()
                )
        );
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new IOException("Không thể lưu tập tin");
        }
        return convertedFile;
    }

    private String getFileName(UUID uuid, String originalFilename){
        return uuid.toString()
                .concat(
                        getExtensionFromFileName(originalFilename)
                );
    }

    private String getExtensionFromFileName(String name){
        return name.substring(
                name.lastIndexOf(".")
        );
    }
}
