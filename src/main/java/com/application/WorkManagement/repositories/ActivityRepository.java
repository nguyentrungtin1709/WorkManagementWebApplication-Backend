package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Activity;
import com.application.WorkManagement.entities.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    List<Activity> findActivitiesByTableOrderByCreatedAtDesc(TableEntity table);

}
