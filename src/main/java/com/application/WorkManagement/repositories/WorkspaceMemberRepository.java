package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.CompositePrimaryKeys.WorkspaceMemberId;
import com.application.WorkManagement.entities.Workspace;
import com.application.WorkManagement.entities.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, WorkspaceMemberId> {

    List<WorkspaceMember> readWorkspaceMembersByAccount(Account account);

    Optional<WorkspaceMember> readWorkspaceMemberByAccountAndWorkspace(Account account, Workspace workspace);

    Boolean existsByAccountAndWorkspace(Account account, Workspace workspace);

    List<WorkspaceMember> readWorkspaceMembersByWorkspaceOrderByWorkspaceRoleDesc(Workspace workspace);

    @Query(
            value = "SELECT members.tk_ma_so, members.kglv_ma_so, members.tvkglv_vai_tro, members.tvkglv_ngay_tao " +
                    "FROM thanh_vien_kglv AS members " +
                    "JOIN tai_khoan AS account " +
                    "ON members.tk_ma_so = account.tk_ma_so " +
                    "WHERE members.kglv_ma_so = :workspaceId AND account.tk_ho_ten LIKE %:keyword% " +
                    "ORDER BY members.tvkglv_vai_tro DESC;",
            nativeQuery = true
    )
    List<WorkspaceMember> readWorkspaceMembersByWorkspace_UuidAndAccount_NameLikeKeywordDESC(
            @Param("workspaceId") UUID workspaceId,
            @Param("keyword") String keyword
    );

}
