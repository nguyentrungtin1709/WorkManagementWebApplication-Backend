package com.application.WorkManagement.services.category;

import com.application.WorkManagement.dto.mappers.card.CardListResponseMapper;
import com.application.WorkManagement.dto.mappers.category.CategoryMapper;
import com.application.WorkManagement.dto.requests.card.CardRequest;
import com.application.WorkManagement.dto.requests.category.CategoryRequest;
import com.application.WorkManagement.dto.responses.card.CardListResponse;
import com.application.WorkManagement.dto.responses.category.CategoryResponse;
import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Activity;
import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.Category;
import com.application.WorkManagement.enums.ActivityType;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.repositories.*;
import com.application.WorkManagement.services.Interface.CategoryService;
import com.application.WorkManagement.services.table.TablePermissionChecker;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final AccountRepository accountRepository;

    private final TablePermissionChecker tablePermissionChecker;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final ActivityRepository activityRepository;

    private final CardListResponseMapper cardListResponseMapper;

    private final CardRepository cardRepository;

    private final CardFollowRepository cardFollowRepository;

    public CategoryServiceImpl(
            AccountRepository accountRepository,
            TablePermissionChecker tablePermissionChecker,
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper,
            ActivityRepository activityRepository,
            CardListResponseMapper cardListResponseMapper,
            CardRepository cardRepository,
            CardFollowRepository cardFollowRepository
    ) {
        this.accountRepository = accountRepository;
        this.activityRepository = activityRepository;
        this.tablePermissionChecker = tablePermissionChecker;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.cardListResponseMapper = cardListResponseMapper;
        this.cardRepository = cardRepository;
        this.cardFollowRepository = cardFollowRepository;
    }


    @Override
    public CategoryResponse readCategoryById(
            String accountId,
            UUID categoryId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Category category = getCategoryById(categoryId);
        tablePermissionChecker.checkReadPermission(account, category.getTable());
        return categoryMapper.apply(
            category
        );
    }

    @Override
    public CategoryResponse updateCategoryById(
            String accountId,
            UUID categoryId,
            CategoryRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        Category category = getCategoryById(categoryId);
        tablePermissionChecker.checkManageComponentsInTablePermission(account, category.getTable());
        if (categoryRepository
                .existsCategoryByTableAndName(category.getTable(), request.getName()) && !category.getName().equals(request.getName())
        ){
            throw new CustomDuplicateException("Danh mục đã tồn tại");
        }
        category.setName(request.getName());
        return categoryMapper.apply(
                categoryRepository.save(category)
        );
    }

    @Override
    public void deleteCategoryById(String accountId, UUID categoryId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Category category = getCategoryById(categoryId);
        tablePermissionChecker.checkManageComponentsInTablePermission(account, category.getTable());
        categoryRepository.deleteById(category.getUuid());
        activityRepository.save(
                Activity
                        .builder()
                        .activityType(ActivityType.DELETE_CATEGORY)
                        .account(account)
                        .table(category.getTable())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public CardListResponse createCardInCategory(String accountId, UUID categoryId, CardRequest request) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Category category = getCategoryById(categoryId);
        tablePermissionChecker.checkManageComponentsInTablePermission(account, category.getTable());
        Card card = cardRepository.save(
                Card.builder()
                        .name(request.getName())
                        .location(category.getNumberOfCards() + 1)
                        .account(account)
                        .category(category)
                        .build()

        );
        category.setNumberOfCards(category.getNumberOfCards() + 1);
        category = categoryRepository.save(category);
        activityRepository.save(
                Activity
                        .builder()
                        .activityType(ActivityType.CREATE_CARD)
                        .createdAt(LocalDateTime.now())
                        .account(account)
                        .table(category.getTable())
                        .category(category)
                        .card(card)
                        .build()
        );
        return cardListResponseMapper.apply(card, false);
    }

    @Override
    public List<CardListResponse> readCardsInCategory(String accountId, UUID categoryId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Category category = getCategoryById(categoryId);
        tablePermissionChecker.checkReadPermission(account, category.getTable());
        return cardRepository
                .findCardsByCategoryOrderByLocation(category)
                .stream()
                .map(card -> {
                    Boolean isFollow = cardFollowRepository.existsCardFollowByAccountAndCard(account, card);
                    return cardListResponseMapper.apply(card, isFollow);
                })
                .collect(Collectors.toList());
    }

    private Account getAccountFromAuthenticationName(String accountId) throws DataNotFoundException {
        return accountRepository
                .findById(UUID.fromString(accountId))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
    }

    private Category getCategoryById(UUID categoryId) throws DataNotFoundException {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy danh mục"));
    }
}
