package springboot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springboot.dto.category.CategoryDto;
import springboot.dto.category.CreateCategoryRequestDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.mapper.CategoryMapper;
import springboot.model.Category;
import springboot.repository.CategoryRepository;
import springboot.service.impl.CategoryServiceImpl;
import springboot.testutil.CategoryUtil;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CreateCategoryRequestDto requestDto;
    private Pageable pageable;
    private Page<Category> categoryPage;

    @BeforeEach
    void setUp() {
        category = CategoryUtil.createCategory();
        categoryDto = CategoryUtil.createCategoryDto(category);
        requestDto = CategoryUtil.createCategoryRequestDto();

        pageable = CategoryUtil.createPageable();
        categoryPage = CategoryUtil.createCategoryPage(pageable);
    }

    @DisplayName("Create a category and return CategoryDto")
    @Test
    public void saveCategory_WithValidData_ReturnsCategoryDto() {
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto savedCategory = categoryService.save(requestDto);

        assertThat(savedCategory).isEqualTo(categoryDto);
        assertThat(savedCategory).isNotNull();
        verify(categoryRepository).save(category);
    }

    @DisplayName("Throws NullPointerException when has is null")
    @Test
    public void createCategory_WithNull_ThrowsException() {
        String exceptionMessage = "Category is null";
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();

        when(categoryMapper.toModel(requestDto))
                .thenThrow(new NullPointerException(exceptionMessage));
        Exception exception = assertThrows(NullPointerException.class, () ->
                categoryService.save(requestDto));

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @DisplayName("Return sorted list of CategoryDto")
    @Test
    void findAll_ReturnCategoryDtoPage() {
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDto);
        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(categoryDto);
    }

    @DisplayName("Return CategoryDto when Category exists")
    @Test
    void getCategoryById_WithExistingId_ReturnCategoryDto() {
        Long categoryId = 101L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(categoryId);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(categoryDto);
    }

    @DisplayName("Should throw EntityNotFoundException when Category does not exist")
    @Test
    void getCategoryById_WithNonExistingId_ThrowException() {
        String exceptionMs = "Cannot find Category with id: ";
        Long categoryId = 999L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getCategoryById(categoryId));

        assertThat(exception.getMessage()).isEqualTo(exceptionMs + categoryId);
    }

    @Test
    @DisplayName("Update Category and return updated CategoryDto when Category exists")
    void updateCategory_WithValidId_ReturnUpdatedCategoryDto() {
        Long categoryId = 101L;

        CreateCategoryRequestDto updateCategoryDto = CategoryUtil.createCategoryRequestDto();
        updateCategoryDto.setDescription("Updated Description");
        updateCategoryDto.setName("Updated Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategoryFromDto(updateCategoryDto, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(
                new CategoryDto() {{
                    setId(categoryId);
                    setDescription("Updated Description");
                    setName("Updated Name");
                }}
        );

        CategoryDto result = categoryService.updateCategoryById(updateCategoryDto, categoryId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoryId);
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        assertThat(result.getName()).isEqualTo("Updated Name");
    }

    @Test
    @DisplayName("Throw EntityNotFoundException when Category does not exist")
    void updateCategory_WithInvalidId_ThrowException() {
        Long categoryId = 999L;
        CreateCategoryRequestDto updateCategoryDto = CategoryUtil.createCategoryRequestDto();
        updateCategoryDto.setDescription("Updated Description");
        updateCategoryDto.setName("Updated Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.updateCategoryById(updateCategoryDto, categoryId));

        assertThat(exception.getMessage()).isEqualTo("Cannot find Category with id: " + categoryId);
    }

    @Test
    @DisplayName("Delete Category when Category exists")
    void deleteById_WithValidId_DeleteCategory() {
        Long categoryId = 101L;

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(categoryId);

        categoryService.deleteCategoryById(categoryId);

    }

    @Test
    @DisplayName("Throw EntityNotFoundException when Category does not exist")
    void deleteById_WithInvalidId_ThrowException() {
        Long categoryId = 999L;
        String expectedMessage = "Cannot find Category with id: " + categoryId;

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteCategoryById(categoryId));

        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
}
