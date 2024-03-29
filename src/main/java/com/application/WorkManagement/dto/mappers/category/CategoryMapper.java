package com.application.WorkManagement.dto.mappers.category;

import com.application.WorkManagement.dto.responses.category.CategoryResponse;
import com.application.WorkManagement.entities.Category;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategoryMapper implements Function<Category, CategoryResponse> {

    @Override
    public CategoryResponse apply(Category category) {
        return CategoryResponse
                .builder()
                .id(category.getUuid())
                .name(category.getName())
                .position(category.getPosition())
                .color(category.getColor())
                .numberOfCards(category.getNumberOfCards())
                .createdAt(category.getCreatedAt())
                .tableId(category.getTable().getUuid())
                .build();
    }

}
