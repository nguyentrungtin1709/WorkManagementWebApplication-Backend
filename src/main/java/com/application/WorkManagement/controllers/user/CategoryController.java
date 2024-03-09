package com.application.WorkManagement.controllers.user;

import com.application.WorkManagement.dto.requests.category.CategoryRequest;
import com.application.WorkManagement.dto.responses.category.CategoryResponse;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.services.Interface.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> readCategoryById(
            JwtAuthenticationToken authentication,
            @PathVariable("categoryId") UUID categoryId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        categoryService.readCategoryById(
                                authentication.getName(),
                                categoryId
                        )
                );
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategoryById(
            JwtAuthenticationToken authentication,
            @PathVariable("categoryId") UUID categoryId,
            @Valid @RequestBody CategoryRequest request
    ) throws DataNotFoundException, CustomDuplicateException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        categoryService.updateCategoryById(
                                authentication.getName(),
                                categoryId,
                                request
                        )
                );
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(
            JwtAuthenticationToken authentication,
            @PathVariable("categoryId") UUID categoryId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        categoryService.deleteCategoryById(
                authentication.getName(),
                categoryId
        );
        return ResponseEntity
                .noContent()
                .build();
    }

}
