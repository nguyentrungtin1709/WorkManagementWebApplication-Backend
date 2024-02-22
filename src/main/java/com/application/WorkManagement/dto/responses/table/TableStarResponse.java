package com.application.WorkManagement.dto.responses.table;

import lombok.*;

import java.net.URI;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableStarResponse {

    private UUID id;

    private String name;

    private URI background;

    private UUID workspaceId;

}
