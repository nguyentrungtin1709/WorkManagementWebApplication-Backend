package com.application.WorkManagement.dto.responses.card;

import com.application.WorkManagement.enums.CardProgress;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {

    private UUID id;

    private String name;

    private String description;

    private CardProgress progress;

    private Integer location;

    private LocalDateTime createdAt;

    private UUID categoryId;

    private Boolean isFollow;

    private DeadlineResponse deadline;

}
