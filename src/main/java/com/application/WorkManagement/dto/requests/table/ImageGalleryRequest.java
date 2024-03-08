package com.application.WorkManagement.dto.requests.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageGalleryRequest {

    @NotNull(message = "Không được bỏ trống")
    private UUID id;

}
