package com.application.WorkManagement.services.table;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.TableEntity;
import com.application.WorkManagement.entities.Workspace;
import com.application.WorkManagement.enums.TableRole;
import com.application.WorkManagement.enums.WorkspaceRole;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.repositories.TableMemberRepository;
import com.application.WorkManagement.repositories.WorkspaceMemberRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TableRoleChecker {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    private final TableMemberRepository tableMemberRepository;

    public TableRoleChecker(
            WorkspaceMemberRepository workspaceMemberRepository,
            TableMemberRepository tableMemberRepository
    ) {
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.tableMemberRepository = tableMemberRepository;
    }

    private void checkRoleInWorkspace(
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

    private Boolean isRoleInWorkspace(
            Account account,
            Workspace workspace,
            Collection<WorkspaceRole> roles
    ) {
        return workspaceMemberRepository.existsWorkspaceMemberByAccountAndWorkspaceAndWorkspaceRoleIn(
                account,
                workspace,
                roles
        );
    }

    public void checkAdminRoleInWorkspace(
            Account account,
            Workspace workspace
    ) throws CustomAccessDeniedException {
        checkRoleInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.ADMIN)
        );
    }

    public void checkMemberRoleInWorkspace(
            Account account,
            Workspace workspace
    ) throws CustomAccessDeniedException {
        checkRoleInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.MEMBER, WorkspaceRole.ADMIN)
        );
    }

    public void checkHasAnyRoleInWorkspace(
            Account account,
            Workspace workspace
    ) throws CustomAccessDeniedException {
        checkRoleInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.OBSERVER, WorkspaceRole.MEMBER, WorkspaceRole.ADMIN)
        );
    }

    public Boolean isAdminRoleInWorkspace(
            Account account,
            Workspace workspace
    ){
        return isRoleInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.ADMIN)
        );
    }

    public Boolean isMemberRoleInWorkspace(
            Account account,
            Workspace workspace
    ){
        return isRoleInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.MEMBER, WorkspaceRole.ADMIN)
        );
    }

    public Boolean isAnyRoleInWorkspace(
            Account account,
            Workspace workspace
    ){
        return isRoleInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.OBSERVER, WorkspaceRole.MEMBER, WorkspaceRole.ADMIN)
        );
    }

    private void checkRoleInTable(
            Account account,
            TableEntity table,
            Collection<TableRole> roles
    ) throws CustomAccessDeniedException {
        Boolean exist = tableMemberRepository.existsTableMemberByAccountAndTableAndTableRoleIn(
                account,
                table,
                roles
        );
        if (!exist){
            throw new CustomAccessDeniedException("Truy cập tài nguyên bị từ chối");
        }
    }

    private Boolean isRoleInTable(
            Account account,
            TableEntity table,
            Collection<TableRole> roles
    ) {
        return tableMemberRepository.existsTableMemberByAccountAndTableAndTableRoleIn(
                account,
                table,
                roles
        );
    }

    public void checkAdminRoleInTable(
            Account account,
            TableEntity table
    ) throws CustomAccessDeniedException {
        checkRoleInTable(
                account,
                table,
                List.of(TableRole.ADMIN)
        );
    }

    public void checkMemberRoleInTable(
            Account account,
            TableEntity table
    ) throws CustomAccessDeniedException {
        checkRoleInTable(
                account,
                table,
                List.of(TableRole.MEMBER, TableRole.ADMIN)
        );
    }

    public void checkHasAnyRoleInTable(
            Account account,
            TableEntity table
    ) throws CustomAccessDeniedException {
        checkRoleInTable(
                account,
                table,
                List.of(TableRole.OBSERVER, TableRole.MEMBER, TableRole.ADMIN)
        );
    }

    public Boolean isAdminRoleInTable(
            Account account,
            TableEntity table
    ){
        return isRoleInTable(
                account,
                table,
                List.of(TableRole.ADMIN)
        );
    }

    public Boolean isMemberRoleInTable(
            Account account,
            TableEntity table
    ){
        return isRoleInTable(
                account,
                table,
                List.of(TableRole.MEMBER, TableRole.ADMIN)
        );
    }

    public Boolean isAnyRoleInTable(
            Account account,
            TableEntity table
    ){
        return isRoleInTable(
                account,
                table,
                List.of(TableRole.OBSERVER, TableRole.MEMBER, TableRole.ADMIN)
        );
    }

}
