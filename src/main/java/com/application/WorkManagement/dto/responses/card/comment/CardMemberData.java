package com.application.WorkManagement.dto.responses.card.comment;

import lombok.*;

import java.net.URI;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardMemberData {

    private UUID id;

    private String name;

    private String email;

    private URI avatar;

}
