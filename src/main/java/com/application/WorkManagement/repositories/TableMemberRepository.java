package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.CompositePrimaryKeys.TableCompositeId;
import com.application.WorkManagement.entities.TableMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMemberRepository extends JpaRepository<TableMember, TableCompositeId> {



}
