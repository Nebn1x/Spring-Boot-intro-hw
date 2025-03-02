package springboot.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springboot.dto.book.BookDto;
import springboot.dto.book.CreateBookRequestDto;
import springboot.model.Book;

public interface BookService {

    BookDto createBook(CreateBookRequestDto requestDto);

    Page<BookDto> getAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto updateBookById(CreateBookRequestDto requestDto, Long id);

    void deleteBookById(Long id);

    List<Book> findAllByCategoryId(Long categoryId);

}
