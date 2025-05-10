package springboot.testutil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import springboot.dto.book.BookDto;
import springboot.dto.book.CreateBookRequestDto;
import springboot.dto.category.CategoryDto;
import springboot.dto.category.CreateCategoryRequestDto;
import springboot.model.Book;
import springboot.model.Category;

public class CategoryUtil {

    public static Category createCategory() {
        Long id = 1L;
        String name = "1";
        String description = "Description";
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setDeleted(false);
        return category;
    }

    public static Category getCategory(CreateCategoryRequestDto requestDto) {
        Category category = new Category();
        category.setId(1L);
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        category.setDeleted(false);

        return category;
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        String name = "Category1";
        String description = "Description";
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(name);
        requestDto.setDescription(description);
        return requestDto;
    }

    public static List<CategoryDto> getCategories() {
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setId(101L);
        categoryDto1.setName("Category1");
        categoryDto1.setDescription("Description1");

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto1.setId(102L);
        categoryDto1.setName("Category2");
        categoryDto1.setDescription("Description2");

        CategoryDto categoryDto3 = new CategoryDto();
        categoryDto1.setId(103L);
        categoryDto1.setName("Category3");
        categoryDto1.setDescription("Description3");

        return List.of(categoryDto1, categoryDto2, categoryDto3);
    }

    public static CategoryDto getCategoryDto(Long testId) {
        String testName = "NewCategoryName1";
        String testDescription = "NewCategoryDescription1";

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(testId);
        expectedDto.setName(testName);
        expectedDto.setDescription(testDescription);
        return expectedDto;
    }

    public static CategoryDto createCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }

    public static Pageable createPageable() {
        return PageRequest.of(0, 2, Sort.by("name"));
    }

    public static Page<Category> createCategoryPage(Pageable pageable) {
        Category category = createCategory();
        return new PageImpl<>(List.of(category), pageable, 1);
    }
}
