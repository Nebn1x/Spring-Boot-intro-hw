package springboot.service;

import java.util.List;
import springboot.dto.BookDto;
import springboot.dto.CreateBookRequestDto;

public interface BookService {
    BookDto createBook(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto getBookById(Long id);
}
