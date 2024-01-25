package com.application.WorkManagement.dto.mappers.workspace;

import com.application.WorkManagement.dto.responses.workspace.MemberResponse;
import com.application.WorkManagement.entities.WorkspaceMember;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WorkspaceMemberMapper implements Function<WorkspaceMember, MemberResponse> {
    @Override
    public MemberResponse apply(WorkspaceMember workspaceMember) {
        return MemberResponse
                .builder()
                .id(workspaceMember.getAccount().getUuid())
                .name(workspaceMember.getAccount().getName())
                .email(workspaceMember.getAccount().getEmail())
                .avatar(workspaceMember.getAccount().getAvatar())
                .role(workspaceMember.getWorkspaceRole())
                .build();
    }
}
