package com.application.WorkManagement.dto.mappers.workspace;

import com.application.WorkManagement.dto.responses.workspace.WorkspaceResponse;
import com.application.WorkManagement.entities.WorkspaceMember;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WorkspaceMapper implements Function<WorkspaceMember, WorkspaceResponse> {
    @Override
    public WorkspaceResponse apply(WorkspaceMember workspaceMember) {
        return WorkspaceResponse
                .builder()
                .id(workspaceMember.getWorkspace().getUuid())
                .name(workspaceMember.getWorkspace().getName())
                .description(workspaceMember.getWorkspace().getDescription())
                .background(workspaceMember.getWorkspace().getBackground())
                .createdAt(workspaceMember.getWorkspace().getCreatedAt())
                .role(workspaceMember.getWorkspaceRole())
                .build();
    }
}
