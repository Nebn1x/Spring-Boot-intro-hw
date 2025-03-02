package springboot.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import springboot.dto.book.BookDtoWithoutCategoryIds;
import springboot.dto.category.CategoryDto;
import springboot.dto.category.CreateCategoryRequestDto;

public interface CategoryService {

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(CreateCategoryRequestDto requestDto, Long id);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id);
}
