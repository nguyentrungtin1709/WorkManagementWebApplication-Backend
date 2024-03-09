package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Category;
import com.application.WorkManagement.entities.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Boolean existsCategoryByTableAndName(TableEntity table, String name);

    List<Category> findCategoriesByTableOrderByCreatedAtDesc(TableEntity table);

}
