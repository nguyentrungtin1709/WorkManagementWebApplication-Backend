package com.application.WorkManagement.dto.mappers.workspace;

import com.application.WorkManagement.dto.responses.workspace.WorkspaceResponse;
import com.application.WorkManagement.entities.WorkspaceInviteCode;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WorkspaceInviteCodeMapper implements Function<WorkspaceInviteCode, WorkspaceResponse> {
    @Override
    public WorkspaceResponse apply(WorkspaceInviteCode inviteCode) {
        return WorkspaceResponse
                .builder()
                .id(inviteCode.getWorkspace().getUuid())
                .name(inviteCode.getWorkspace().getName())
                .description(inviteCode.getWorkspace().getDescription())
                .createdAt(inviteCode.getWorkspace().getCreatedAt())
                .role(inviteCode.getWorkspaceRole())
                .build();
    }
}
