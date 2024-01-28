package com.application.WorkManagement.services;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Workspace;
import com.application.WorkManagement.enums.WorkspaceRole;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.repositories.WorkspaceMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class WorkspacePermissionChecker {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Autowired
    public WorkspacePermissionChecker(WorkspaceMemberRepository workspaceMemberRepository) {
        this.workspaceMemberRepository = workspaceMemberRepository;
    }


    private void checkPermissionInWorkspace(
            Account account,
            Workspace workspace,
            Collection<WorkspaceRole> roles
    ) throws CustomAccessDeniedException {
        Boolean exist = workspaceMemberRepository
                .existsWorkspaceMemberByAccountAndWorkspaceAndWorkspaceRoleIn(
                        account,
                        workspace,
                        roles
                );
        if (!exist){
            throw new CustomAccessDeniedException("Truy cập tài nguyên bị từ chối");
        }
    }

    public void checkAdminPermissionInWorkspace(
            Account account,
            Workspace workspace
    ) throws CustomAccessDeniedException {
        checkPermissionInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.ADMIN)
        );
    }

    public void checkMemberPermissionInWorkspace(
            Account account,
            Workspace workspace
    ) throws CustomAccessDeniedException {
        checkPermissionInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.MEMBER, WorkspaceRole.ADMIN)
        );
    }

    public void checkHasAnyPermissionInWorkspace(
            Account account,
            Workspace workspace
    ) throws CustomAccessDeniedException {
        checkPermissionInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.OBSERVER, WorkspaceRole.MEMBER, WorkspaceRole.ADMIN)
        );
    }

}
