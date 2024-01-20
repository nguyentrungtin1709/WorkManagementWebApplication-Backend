package com.application.WorkManagement.dto.responses;

import lombok.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private UUID uuid;

    private String name;

    private String email;

    private URI avatar;

    private String organization;

    private String department;

    private String title;

    private LocalDateTime notification;

}
