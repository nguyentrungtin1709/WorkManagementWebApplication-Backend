package com.application.WorkManagement.dto.responses.account;

import com.application.WorkManagement.enums.UserRole;
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

    private UUID id;

    private String name;

    private String email;

    private URI avatar;

    private String organization;

    private String department;

    private String title;

    private UserRole role;

    private LocalDateTime notification;

}
