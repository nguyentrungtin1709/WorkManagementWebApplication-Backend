package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.CompositePrimaryKeys.WorkspaceMemberId;
import com.application.WorkManagement.entities.WorkspaceInviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceInviteCodeRepository extends JpaRepository<WorkspaceInviteCode, WorkspaceMemberId> {

}
