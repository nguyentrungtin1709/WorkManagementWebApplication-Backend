package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.CompositePrimaryKeys.TableCompositeId;
import com.application.WorkManagement.entities.TableEntity;
import com.application.WorkManagement.entities.TableStar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableStarRepository extends JpaRepository<TableStar, TableCompositeId> {

    Optional<TableStar> findTableStarByAccountAndTable(Account account, TableEntity table);

    List<TableStar> findTableStarsByAccount(Account account);

    void deleteTableStarByAccountAndTable(Account account, TableEntity table);

}
