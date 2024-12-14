package springboot.repository;

import java.util.List;
import java.util.Optional;
import springboot.dto.BookDto;
import springboot.dto.CreateBookRequestDto;
import springboot.model.Book;

public interface BookRepository {
    BookDto createBook(CreateBookRequestDto requestDto);

    List<Book> getAll();

    Optional<Book> getBookById(Long id);
}
