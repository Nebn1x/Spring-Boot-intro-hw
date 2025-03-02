package springboot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import springboot.config.MapperConfig;
import springboot.dto.category.CategoryDto;
import springboot.dto.category.CreateCategoryRequestDto;
import springboot.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    void updateCategoryFromDto(CreateCategoryRequestDto requestDto,
                               @MappingTarget Category category);
}
