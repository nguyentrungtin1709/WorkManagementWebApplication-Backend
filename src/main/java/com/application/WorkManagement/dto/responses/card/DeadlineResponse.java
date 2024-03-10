package com.application.WorkManagement.dto.responses.card;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeadlineResponse {

    private UUID id;

    private LocalDateTime deadline;

    private LocalDateTime reminderDate;

    private Boolean complete;

}
