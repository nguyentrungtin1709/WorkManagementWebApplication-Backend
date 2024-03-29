package com.application.WorkManagement.controllers.user;

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
import com.application.WorkManagement.services.Interface.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PatchMapping("/{categoryId}/color")
    public ResponseEntity<CategoryResponse> updateBackgroundColorOfCategory(
            JwtAuthenticationToken authentication,
            @PathVariable("categoryId") UUID categoryId,
            @Valid @RequestBody CategoryColorRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        categoryService.updateBackgroundColorOfCategory(
                            authentication.getName(),
                            categoryId,
                            request
                        )
                );
    }

    @PatchMapping("/{categoryId}/position")
    public ResponseEntity<CategoryResponse> updatePositionOfCategory(
            JwtAuthenticationToken authentication,
            @PathVariable("categoryId") UUID categoryId,
            @Valid @RequestBody CategoryPositionRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, InvalidPositionException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        categoryService.updatePositionOfCategory(
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

    @PostMapping("/{categoryId}/cards")
    public ResponseEntity<CardListResponse> createCardInCategory(
            @PathVariable("categoryId") UUID categoryId,
            JwtAuthenticationToken authentication,
            @Valid @RequestBody CardRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    categoryService.createCardInCategory(
                            authentication.getName(),
                            categoryId,
                            request
                    )
                );
    }

    @GetMapping("/{categoryId}/cards")
    public ResponseEntity<List<CardListResponse>> readCardsInCategory(
        JwtAuthenticationToken authentication,
        @PathVariable("categoryId") UUID categoryId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        categoryService.readCardsInCategory(
                                authentication.getName(),
                                categoryId
                        )
                );
    }

}
