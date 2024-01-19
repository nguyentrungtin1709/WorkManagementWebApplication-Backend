package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.CardMember;
import com.application.WorkManagement.entities.CompositePrimaryKeys.CardCompositeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardMemberRepository extends JpaRepository<CardMember, CardCompositeId> {

}
