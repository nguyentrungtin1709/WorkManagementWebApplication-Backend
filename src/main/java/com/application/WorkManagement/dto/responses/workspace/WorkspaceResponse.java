package com.application.WorkManagement.dto.responses.workspace;

import com.application.WorkManagement.enums.WorkspaceRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceResponse {

    private UUID id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    private WorkspaceRole role;

}
