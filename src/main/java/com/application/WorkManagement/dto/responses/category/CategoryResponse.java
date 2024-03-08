package com.application.WorkManagement.dto.responses.category;

import com.application.WorkManagement.entities.TableEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private UUID id;

    private String name;

    private Integer numberOfCards;

    private LocalDateTime createdAt;

    private UUID tableId;

}
