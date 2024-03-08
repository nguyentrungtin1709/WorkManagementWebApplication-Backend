package com.application.WorkManagement.services.category;

import com.application.WorkManagement.dto.mappers.category.CategoryMapper;
import com.application.WorkManagement.repositories.AccountRepository;
import com.application.WorkManagement.repositories.CategoryRepository;
import com.application.WorkManagement.repositories.TableRepository;
import com.application.WorkManagement.services.Interface.CategoryService;
import com.application.WorkManagement.services.table.TablePermissionChecker;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final AccountRepository accountRepository;

    private final TableRepository tableRepository;

    private final TablePermissionChecker tablePermissionChecker;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(
            AccountRepository accountRepository,
            TableRepository tableRepository,
            TablePermissionChecker tablePermissionChecker,
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper
    ) {
        this.accountRepository = accountRepository;
        this.tableRepository = tableRepository;
        this.tablePermissionChecker = tablePermissionChecker;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


}
