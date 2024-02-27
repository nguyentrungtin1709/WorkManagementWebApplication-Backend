package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.ImageGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageGalleryRepository extends JpaRepository<ImageGallery, UUID> {
}
