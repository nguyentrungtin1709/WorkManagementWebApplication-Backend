package com.application.WorkManagement.dto.mappers.workspace;

import com.application.WorkManagement.dto.responses.workspace.InviteCodeResponse;
import com.application.WorkManagement.entities.WorkspaceInviteCode;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.UUID;
import java.util.function.Function;

@Service
public class InviteCodeMapper implements Function<WorkspaceInviteCode, InviteCodeResponse> {

    @Override
    public InviteCodeResponse apply(WorkspaceInviteCode workspaceInviteCode) {
        UUID workspaceId = workspaceInviteCode.getWorkspace().getUuid();
        UUID inviteCode = workspaceInviteCode.getInviteCode();
        return InviteCodeResponse
                .builder()
                .workspaceId(workspaceId)
                .inviteCode(inviteCode)
                .inviteUrl(URI.create(
                        String.format(
                                "/workspaces/%s/invitations/%s",
                                workspaceId.toString(),
                                inviteCode.toString()
                        )
                ))
                .build();
    }

}
