package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ListRepository extends JpaRepository<ListEntity, UUID> {

    List<ListEntity> findListEntitiesByCardOrderByCreatedAtDesc(Card card);

}
