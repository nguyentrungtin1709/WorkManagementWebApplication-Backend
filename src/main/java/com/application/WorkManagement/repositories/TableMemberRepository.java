package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.CompositePrimaryKeys.TableCompositeId;
import com.application.WorkManagement.entities.TableEntity;
import com.application.WorkManagement.entities.TableMember;
import com.application.WorkManagement.enums.TableRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TableMemberRepository extends JpaRepository<TableMember, TableCompositeId> {

    Optional<TableMember> findTableMemberByAccountAndTable(Account account, TableEntity table);

    Boolean existsTableMemberByAccountAndTableAndTableRoleIn(Account account, TableEntity table, Collection<TableRole> roles);

    Boolean existsTableMemberByAccountAndTable(Account account, TableEntity table);

}
