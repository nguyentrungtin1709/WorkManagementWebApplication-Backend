package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    List<Card> findCardsByCategoryOrderByLocation(Category category);

}
