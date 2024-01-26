package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.CompositePrimaryKeys.WorkspaceMemberId;
import com.application.WorkManagement.entities.Workspace;
import com.application.WorkManagement.entities.WorkspaceInviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceInviteCodeRepository extends JpaRepository<WorkspaceInviteCode, WorkspaceMemberId> {

    Boolean existsByAccountAndWorkspace(Account account, Workspace workspace);

    Optional<WorkspaceInviteCode> findWorkspaceInviteCodeByAccountAndWorkspace(Account account, Workspace workspace);

    void deleteWorkspaceInviteCodeByAccountAndWorkspace(Account account, Workspace workspace);

    Optional<WorkspaceInviteCode> findWorkspaceInviteCodeByWorkspace_UuidAndInviteCode(UUID workspaceId, UUID inviteCode);

}
