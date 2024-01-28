package com.application.WorkManagement.dto.responses.table;

import com.application.WorkManagement.enums.TableRole;
import com.application.WorkManagement.enums.TableScope;
import lombok.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableEntityResponse {

    private UUID id;

    private String name;

    private String description;

    private TableScope scope;

    private URI background;

    private TableRole role;

    private UUID workspaceId;

    private LocalDateTime createdAt;

}
