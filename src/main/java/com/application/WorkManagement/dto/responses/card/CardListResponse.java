package com.application.WorkManagement.dto.responses.card;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardListResponse {

    private UUID id;

    private String name;

    private Integer location;

    private LocalDateTime createdAt;

    private UUID categoryId;

    private Boolean isFollow;

    private DeadlineResponse deadline;

}
