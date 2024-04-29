package com.application.WorkManagement.dto.responses.card.comment;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardCommentResponse {

    private UUID id;

    private CardMemberData member;

    private String comment;

    private Boolean update;

    private LocalDateTime createdAt;

}
