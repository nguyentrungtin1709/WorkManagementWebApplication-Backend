package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.TableEntity;
import com.application.WorkManagement.entities.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, UUID> {

    Boolean existsTableEntityByWorkspaceAndName(Workspace workspace, String name);

    List<TableEntity> findTableEntitiesByWorkspace(Workspace workspace);

    @Query(
            value = "SELECT DISTINCT bang.* " +
                    "FROM bang " +
                    "LEFT JOIN thanh_vien_bang " +
                    "ON bang.bang_ma_so = thanh_vien_bang.bang_ma_so " +
                    "WHERE bang.kglv_ma_so = :workspaceId AND bang.bang_pham_vi = 'WORKSPACE' OR thanh_vien_bang.tk_ma_so = :accountId AND bang.kglv_ma_so = :workspaceId AND bang.bang_pham_vi = 'GROUP'",
            nativeQuery = true
    )
    List<TableEntity> findTableEntitiesInWorkspace(UUID workspaceId, UUID accountId);
}
