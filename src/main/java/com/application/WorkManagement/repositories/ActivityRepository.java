package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Activity;
import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.TableEntity;
import com.application.WorkManagement.enums.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    List<Activity> findActivitiesByTableOrderByCreatedAtDesc(TableEntity table);

    List<Activity> findActivitiesByCardIn(Collection<Card> cards);

    void deleteActivitiesByActivityTypeAndCard(ActivityType type, Card card);

}
