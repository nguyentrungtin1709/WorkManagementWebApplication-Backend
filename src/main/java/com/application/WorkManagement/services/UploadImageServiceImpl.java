package com.application.WorkManagement.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.application.WorkManagement.entities.Avatar;
import com.application.WorkManagement.enums.ImageType;
import com.application.WorkManagement.exceptions.custom.EmptyImageException;
import com.application.WorkManagement.exceptions.custom.InvalidFileExtensionException;
import com.application.WorkManagement.repositories.AvatarRepository;
import com.application.WorkManagement.services.Interface.UploadImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private AvatarRepository avatarRepository;

    private AmazonS3 s3Client;

    @Value("${cloud.s3.bucket.name}")
    private String bucketName;

    public UploadImageServiceImpl(AvatarRepository avatarRepository, AmazonS3 s3Client) {
        this.avatarRepository = avatarRepository;
        this.s3Client = s3Client;
    }

    @Override
    @Transactional(rollbackFor = {IOException.class, URISyntaxException.class})
    public URI uploadImageToS3(MultipartFile file) throws EmptyImageException, InvalidFileExtensionException, URISyntaxException, IOException {
        if (file.isEmpty()){
            throw new EmptyImageException("Không có dữ liệu hình ảnh");
        }
        String contentType = file.getContentType();;
        if (!ImageType.PNG.toString().equals(contentType) && !ImageType.JPEG.toString().equals(contentType)){
            throw new InvalidFileExtensionException("Định dạng hình ảnh không hợp lệ");
        }
        Avatar avatar = avatarRepository.save(
                Avatar.builder()
                        .originalFileName(file.getOriginalFilename())
                        .contentType(contentType)
                        .size(file.getSize())
                        .build()
        );
        File object = convertMultiPartFileToFile(file);
        String fileName = getFileName(avatar.getUuid(), file.getOriginalFilename());
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, object));
        boolean isDeleted = object.delete();
        return s3Client.getUrl(bucketName, fileName).toURI();
    }

    @Override
    public void removeFileFromS3(String fileName) throws URISyntaxException {
        s3Client.deleteObject(bucketName, fileName);
        UUID uuid = UUID.fromString(fileName.substring(0, fileName.lastIndexOf(".")));
        avatarRepository.deleteById(uuid);
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

    public String getFileName(UUID uuid, String originalFilename){
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
