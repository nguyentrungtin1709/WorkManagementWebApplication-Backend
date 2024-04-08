package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.CompositePrimaryKeys.TableCompositeId;
import com.application.WorkManagement.entities.TableEntity;
import com.application.WorkManagement.entities.TableMember;
import com.application.WorkManagement.enums.TableRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TableMemberRepository extends JpaRepository<TableMember, TableCompositeId> {

    Optional<TableMember> findTableMemberByAccountAndTable(Account account, TableEntity table);

    Boolean existsTableMemberByAccountAndTableAndTableRoleIn(Account account, TableEntity table, Collection<TableRole> roles);

    Boolean existsTableMemberByAccountAndTable(Account account, TableEntity table);

    List<TableMember> findTableMembersByTableOrderByTableRoleDesc(TableEntity table);

    List<TableMember> findTableMembersByTable(TableEntity table);

    void deleteTableMemberByAccountAndTable(Account account, TableEntity table);

    @Query(
            value = "DELETE thanh_vien_the FROM thanh_vien_the JOIN the ON thanh_vien_the.the_ma_so = the.the_ma_so JOIN danh_muc ON the.dm_ma_so = danh_muc.dm_ma_so JOIN bang ON danh_muc.bang_ma_so = bang.bang_ma_so WHERE bang.bang_ma_so = :tableId AND thanh_vien_the.tk_ma_so = :memberId",
            nativeQuery = true
    )
    @Modifying
    void deleteCardMemberByAccount_UuidAndTable_Uuid(UUID memberId, UUID tableId);

    @Query(
            value = "DELETE theo_doi_the FROM theo_doi_the JOIN the ON theo_doi_the.the_ma_so = the.the_ma_so JOIN danh_muc ON the.dm_ma_so = danh_muc.dm_ma_so JOIN bang ON danh_muc.bang_ma_so = bang.bang_ma_so WHERE bang.bang_ma_so = :tableId AND theo_doi_the.tk_ma_so = :memberId",
            nativeQuery = true
    )
    @Modifying
    void deleteCardFollowByAccount_UuidAndTable_Uuid(UUID memberId, UUID tableId);

    List<TableMember> findTableMembersByTableAndAccountNotIn(TableEntity table, Collection<Account> accounts);

}
