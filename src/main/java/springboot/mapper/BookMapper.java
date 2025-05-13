package springboot.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import springboot.config.MapperConfig;
import springboot.dto.book.BookDto;
import springboot.dto.book.BookDtoWithoutCategoryIds;
import springboot.dto.book.CreateBookRequestDto;
import springboot.model.Book;
import springboot.model.Category;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", source = "categories",
            qualifiedByName = "categoryIds")
    BookDto toDto(Book book);

    @Mapping(target = "categories", source = "categoryIds",
            qualifiedByName = "categoriesFromIds")
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "categories", source = "categoryIds",
            qualifiedByName = "categoriesFromIds")
    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        Book book = new Book();
        book.setId(id);
        return book;
    }

    @Named("categoryIds")
    default List<Long> mapCategoryIds(Set<Category> categories) {
        if (categories == null) {
            return List.of();
        }
        return categories.stream()
                .map(Category::getId)
                .toList();
    }

    @Named("categoriesFromIds")
    default Set<Category> mapCategoriesFromIds(List<Long> ids) {
        if (ids == null) {
            return new HashSet<>();
        }
        return ids.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }
}
