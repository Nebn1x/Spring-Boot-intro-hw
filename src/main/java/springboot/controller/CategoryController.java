package springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springboot.dto.category.CategoryDto;
import springboot.dto.category.CreateCategoryRequestDto;
import springboot.service.CategoryService;

@Tag(name = "Categories", description = "Operations related to categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Create a new category",
            description = "Creates a new category. Only available to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Category created successfully"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @Operation(
            summary = "Get all categories",
            description = "Return list of all categories. Accessible to USER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Categories retrieved successfully"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public Page<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(
            summary = "Get category by ID",
            description = "Return a category by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Category retrieved successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Category not found")
            }
    )
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Update a category",
            description = "Updates a category with the given ID. Only available to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Category updated successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Category not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}")
    public CategoryDto updateCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto,
            @PathVariable Long id) {
        return categoryService.update(requestDto, id);
    }

    @Operation(
            summary = "Delete a category",
            description = "Deletes a category by its ID. Only available to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Category deleted successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Category not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
