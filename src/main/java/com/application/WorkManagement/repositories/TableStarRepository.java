package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.CompositePrimaryKeys.TableCompositeId;
import com.application.WorkManagement.entities.TableStar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableStarRepository extends JpaRepository<TableStar, TableCompositeId> {

}
