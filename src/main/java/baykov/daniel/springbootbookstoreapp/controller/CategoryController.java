package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.payload.dto.CategoryDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.CATEGORY_DELETED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
@Tag(name = "CRUD REST APIs for Category Resource")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Create Category REST API",
            description = "Create Category REST API is used to save category into the database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("Correlation ID: {}. Received request to create a new category: {}", RequestData.getCorrelationId(), categoryDTO);

        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);

        log.info("Correlation ID: {}. Category created successfully: {}", RequestData.getCorrelationId(), createdCategory);

        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Category REST API",
            description = "Get Category REST API is used to get a particular category from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId) {
        log.info("Correlation ID: {}. Received request to retrieve category with ID: {}", RequestData.getCorrelationId(), categoryId);

        CategoryDTO category = categoryService.getCategoryById(categoryId);

        log.info("Correlation ID: {}. Retrieved category successfully with ID {}: {}", RequestData.getCorrelationId(), categoryId, category);
        return ResponseEntity.ok(category);
    }

    @Operation(
            summary = "Get All Categories REST API",
            description = "Get All Categories REST API is used to get all categories from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping
    public ResponseEntity<GenericResponse<CategoryDTO>> getAllCategories(
            @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to fetch categories with parameters: pageNo={}, pageSize={}, sortBy={}, sortDir={}",
                RequestData.getCorrelationId(), pageNo, pageSize, sortBy, sortDir);

        GenericResponse<CategoryDTO> categoriesResponse = categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Fetched categories successfully. Total categories retrieved: {}", RequestData.getCorrelationId(), categoriesResponse.getContent().size());
        return ResponseEntity.ok(categoriesResponse);
    }

    @Operation(
            summary = "Update Category REST API",
            description = "Update Category REST API is used to update an existing category in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategoryById(
            @PathVariable Long categoryId, @Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("Correlation ID: {}. Received request to update category with ID {}: {}", RequestData.getCorrelationId(), categoryId, categoryDTO);

        CategoryDTO updatedCategory = categoryService.updateCategoryById(categoryId, categoryDTO);

        log.info("Correlation ID: {}. Category with ID {} updated successfully: {}", RequestData.getCorrelationId(), categoryId, categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(
            summary = "Delete Category REST API",
            description = "Delete Category REST API is used to delete a particular category from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long categoryId) {
        log.info("Correlation ID: {}. Received request to delete category with ID: {}", RequestData.getCorrelationId(), categoryId);

        categoryService.deleteCategoryById(categoryId);

        log.info("Correlation ID: {}. Category with ID {} deleted successfully", RequestData.getCorrelationId(), categoryId);
        return ResponseEntity.ok(CATEGORY_DELETED);
    }
}
