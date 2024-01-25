package com.application.WorkManagement.dto.responses.workspace;

import com.application.WorkManagement.enums.WorkspaceRole;
import lombok.*;

import java.net.URI;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private UUID id;

    private String name;

    private String email;

    private URI avatar;

    private WorkspaceRole role;

}
