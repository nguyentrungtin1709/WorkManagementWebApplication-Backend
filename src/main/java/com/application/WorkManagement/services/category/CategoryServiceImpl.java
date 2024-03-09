package com.application.WorkManagement.services.category;

import com.application.WorkManagement.dto.mappers.category.CategoryMapper;
import com.application.WorkManagement.dto.requests.category.CategoryRequest;
import com.application.WorkManagement.dto.responses.category.CategoryResponse;
import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Activity;
import com.application.WorkManagement.entities.Category;
import com.application.WorkManagement.enums.ActivityType;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.repositories.AccountRepository;
import com.application.WorkManagement.repositories.ActivityRepository;
import com.application.WorkManagement.repositories.CategoryRepository;
import com.application.WorkManagement.repositories.TableRepository;
import com.application.WorkManagement.services.Interface.CategoryService;
import com.application.WorkManagement.services.table.TablePermissionChecker;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final AccountRepository accountRepository;

    private final TablePermissionChecker tablePermissionChecker;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final ActivityRepository activityRepository;

    public CategoryServiceImpl(
            AccountRepository accountRepository,
            TablePermissionChecker tablePermissionChecker,
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper,
            ActivityRepository activityRepository
    ) {
        this.accountRepository = accountRepository;
        this.activityRepository = activityRepository;
        this.tablePermissionChecker = tablePermissionChecker;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
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
