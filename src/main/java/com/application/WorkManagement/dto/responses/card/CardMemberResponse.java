package com.application.WorkManagement.dto.responses.card;

import lombok.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardMemberResponse {

    private UUID id;

    private String name;

    private String email;

    private URI avatar;

    private LocalDateTime createdAt;

}
