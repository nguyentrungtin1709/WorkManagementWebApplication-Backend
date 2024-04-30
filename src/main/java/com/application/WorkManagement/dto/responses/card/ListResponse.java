package com.application.WorkManagement.dto.responses.card;

import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse {

    private UUID id;

    private String name;

    private UUID cardId;

    private LocalDateTime createdAt;

}
