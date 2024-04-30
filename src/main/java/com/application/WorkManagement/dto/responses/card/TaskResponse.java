package com.application.WorkManagement.dto.responses.card;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private UUID id;

    private String name;

    private Boolean complete;

    private UUID listId;

    private UUID cardId;

    private LocalDateTime createdAt;

}
