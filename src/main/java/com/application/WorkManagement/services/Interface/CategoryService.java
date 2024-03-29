package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.card.CardRequest;
import com.application.WorkManagement.dto.requests.category.CategoryColorRequest;
import com.application.WorkManagement.dto.requests.category.CategoryPositionRequest;
import com.application.WorkManagement.dto.requests.category.CategoryRequest;
import com.application.WorkManagement.dto.responses.card.CardListResponse;
import com.application.WorkManagement.dto.responses.category.CategoryResponse;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.exceptions.custom.InvalidPositionException;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse readCategoryById(String accountId, UUID categoryId) throws DataNotFoundException, CustomAccessDeniedException;

    CategoryResponse updateCategoryById(String accountId, UUID categoryId, CategoryRequest request) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException;

    void deleteCategoryById(String accountId, UUID categoryId) throws DataNotFoundException, CustomAccessDeniedException;

    CardListResponse createCardInCategory(String accountId, UUID categoryId, CardRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    List<CardListResponse> readCardsInCategory(String accountId, UUID categoryId) throws DataNotFoundException, CustomAccessDeniedException;

    CategoryResponse updateBackgroundColorOfCategory(String accountId, UUID categoryId, CategoryColorRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    CategoryResponse updatePositionOfCategory(String accountId, UUID categoryId, CategoryPositionRequest request) throws DataNotFoundException, CustomAccessDeniedException, InvalidPositionException;
}
