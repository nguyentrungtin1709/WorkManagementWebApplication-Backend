package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.CompositePrimaryKeys.WorkspaceMemberId;
import com.application.WorkManagement.entities.Workspace;
import com.application.WorkManagement.entities.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, WorkspaceMemberId> {

    List<WorkspaceMember> readWorkspaceMembersByAccount(Account account);

    Optional<WorkspaceMember> readWorkspaceMemberByAccountAndWorkspace(Account account, Workspace workspace);

}
