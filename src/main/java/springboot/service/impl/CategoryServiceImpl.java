package springboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springboot.dto.category.CategoryDto;
import springboot.dto.category.CreateCategoryRequestDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.mapper.CategoryMapper;
import springboot.model.Category;
import springboot.repository.CategoryRepository;
import springboot.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Book with id: " + id));
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(CreateCategoryRequestDto requestDto, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find Book with id: " + id));
        categoryMapper.updateCategoryFromDto(requestDto, category);

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
        throw new EntityNotFoundException("Cannot find Book with id: " + id);
    }
}
