package springboot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import springboot.config.MapperConfig;
import springboot.dto.BookDto;
import springboot.dto.CreateBookRequestDto;
import springboot.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);
}
