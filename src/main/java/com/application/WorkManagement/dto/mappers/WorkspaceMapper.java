package com.application.WorkManagement.dto.mappers;

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
                .createdAt(workspaceMember.getWorkspace().getCreatedAt())
                .role(workspaceMember.getWorkspaceRole())
                .build();
    }
}
