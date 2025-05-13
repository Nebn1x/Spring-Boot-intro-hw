package springboot.testutil;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import springboot.dto.category.CategoryDto;
import springboot.dto.category.CreateCategoryRequestDto;
import springboot.model.Category;

public class CategoryUtil {

    public static Category createCategory() {
        Long id = 1L;
        String name = "1";
        String description = "Description1";
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
        String name = "1";
        String description = "Description1";
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(name);
        requestDto.setDescription(description);
        return requestDto;
    }

    public static CreateCategoryRequestDto createNewCategoryRequestDto() {
        String name = "1Name";
        String description = "Description1";
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(name);
        requestDto.setDescription(description);
        return requestDto;
    }

    public static List<CategoryDto> getCategories() {
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setId(101L);
        categoryDto1.setName("1");
        categoryDto1.setDescription("Description1");

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setId(102L);
        categoryDto2.setName("2");
        categoryDto2.setDescription("Description2");

        return List.of(categoryDto1, categoryDto2);
    }

    public static CategoryDto getCategoryDto(Long testId) {
        String testName = "1";
        String testDescription = "Description1";

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
