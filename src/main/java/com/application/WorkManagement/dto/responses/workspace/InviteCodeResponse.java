package com.application.WorkManagement.dto.responses.workspace;

import lombok.*;

import java.net.URI;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeResponse {

    private UUID workspaceId;

    private UUID inviteCode;

    private URI inviteUrl;

}
