package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeadlineRepository extends JpaRepository<Deadline, UUID> {

    void deleteDeadlineByCard(Card card);

    Deadline findDeadlineByCard(Card card);

    Boolean existsDeadlineByCard(Card card);

}
