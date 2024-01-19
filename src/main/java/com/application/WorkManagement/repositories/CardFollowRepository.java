package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.CardFollow;
import com.application.WorkManagement.entities.CompositePrimaryKeys.CardCompositeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardFollowRepository extends JpaRepository<CardFollow, CardCompositeId> {

}
