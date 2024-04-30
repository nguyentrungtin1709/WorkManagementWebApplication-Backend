package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.ListEntity;
import com.application.WorkManagement.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findTaskEntitiesByListEntityOrderByCreatedAt(ListEntity list);

}
