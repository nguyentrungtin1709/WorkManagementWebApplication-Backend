package com.application.WorkManagement.services;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Workspace;
import com.application.WorkManagement.enums.WorkspaceRole;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.repositories.TableMemberRepository;
import com.application.WorkManagement.repositories.WorkspaceMemberRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TablePermissionChecker {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    private final TableMemberRepository tableMemberRepository;

    public TablePermissionChecker(
            WorkspaceMemberRepository workspaceMemberRepository,
            TableMemberRepository tableMemberRepository
    ) {
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.tableMemberRepository = tableMemberRepository;
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

    private void checkAdminPermissionInWorkspace(
            Account account,
            Workspace workspace
    ) throws CustomAccessDeniedException {
        checkPermissionInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.ADMIN)
        );
    }

}
