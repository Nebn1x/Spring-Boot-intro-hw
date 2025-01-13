package springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springboot.dto.BookDto;
import springboot.dto.CreateBookRequestDto;

public interface BookService {

    BookDto createBook(CreateBookRequestDto requestDto);

    Page<BookDto> getAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto updateBookById(CreateBookRequestDto requestDto, Long id);

    void deleteBookById(Long id);
}
