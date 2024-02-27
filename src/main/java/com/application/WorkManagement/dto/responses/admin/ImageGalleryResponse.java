package com.application.WorkManagement.dto.responses.admin;

import lombok.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageGalleryResponse {

    private UUID id;

    private URI imageUri;

    private LocalDateTime createdAt;

}
