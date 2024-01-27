package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.CompositePrimaryKeys.WorkspaceMemberId;
import com.application.WorkManagement.entities.Workspace;
import com.application.WorkManagement.entities.WorkspaceMember;
import com.application.WorkManagement.enums.WorkspaceRole;
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

    Boolean existsByWorkspaceAndWorkspaceRole(Workspace workspace, WorkspaceRole role);

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

    @Query(
            value = "DELETE FROM thanh_vien_kglv AS tvk WHERE tvk.kglv_ma_so = :workspaceId AND tvk.tk_ma_so = :memberId",
            nativeQuery = true
    )
    @Modifying
    void deleteWorkspaceMemberByAccount_UuidAndWorkspace_UuidCustom(UUID memberId, UUID workspaceId);

    @Query(
            value = "DELETE FROM ma_moi_kglv AS mmk WHERE mmk.kglv_ma_so = :workspaceId AND mmk.tk_ma_so = :memberId",
            nativeQuery = true
    )
    @Modifying
    void deleteInviteCodeByAccount_UuidAndWorkspace_Uuid(UUID memberId, UUID workspaceId);

    @Query(
            value = "DELETE danh_dau_sao FROM danh_dau_sao JOIN bang ON danh_dau_sao.bang_ma_so = bang.bang_ma_so JOIN khong_gian_lam_viec AS kglv ON bang.kglv_ma_so = kglv.kglv_ma_so WHERE danh_dau_sao.tk_ma_so = :memberId AND kglv.kglv_ma_so = :workspaceId",
            nativeQuery = true
    )
    @Modifying
    void deleteTableStarByAccount_UuidAndWorkspace_Uuid(UUID memberId, UUID workspaceId);

    @Query(
            value = "DELETE thanh_vien_bang FROM thanh_vien_bang JOIN bang ON thanh_vien_bang.bang_ma_so = bang.bang_ma_so JOIN khong_gian_lam_viec AS kglv ON bang.kglv_ma_so = kglv.kglv_ma_so WHERE thanh_vien_bang.tk_ma_so = :memberId AND kglv.kglv_ma_so = :workspaceId",
            nativeQuery = true
    )
    @Modifying
    void deleteTableMemberByAccount_UuidAndWorkspace_Uuid(UUID memberId, UUID workspaceId);

    @Query(
            value = "DELETE thanh_vien_the FROM thanh_vien_the JOIN the ON thanh_vien_the.the_ma_so = the.the_ma_so JOIN danh_muc ON the.dm_ma_so = danh_muc.dm_ma_so JOIN bang ON danh_muc.bang_ma_so = bang.bang_ma_so JOIN khong_gian_lam_viec AS kglv ON bang.kglv_ma_so = kglv.kglv_ma_so WHERE kglv.kglv_ma_so = :workspaceId AND thanh_vien_the.tk_ma_so = :memberId",
            nativeQuery = true
    )
    @Modifying
    void deleteCardMemberByAccount_UuidAndWorkspace_Uuid(UUID memberId, UUID workspaceId);

    @Query(
            value = "DELETE theo_doi_the FROM theo_doi_the JOIN the ON theo_doi_the.the_ma_so = the.the_ma_so JOIN danh_muc ON the.dm_ma_so = danh_muc.dm_ma_so JOIN bang ON danh_muc.bang_ma_so = bang.bang_ma_so JOIN khong_gian_lam_viec AS kglv ON bang.kglv_ma_so = kglv.kglv_ma_so WHERE kglv.kglv_ma_so = :workspaceId AND theo_doi_the.tk_ma_so = :memberId",
            nativeQuery = true
    )
    @Modifying
    void deleteCardFollowByAccount_UuidAndWorkspace_Uuid(UUID memberId, UUID workspaceId);

}
