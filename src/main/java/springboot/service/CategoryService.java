package springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springboot.dto.category.CategoryDto;
import springboot.dto.category.CreateCategoryRequestDto;

public interface CategoryService {

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(CreateCategoryRequestDto requestDto, Long id);

    void deleteById(Long id);
}
