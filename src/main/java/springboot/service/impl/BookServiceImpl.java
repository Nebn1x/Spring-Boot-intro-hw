package springboot.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.model.Book;
import springboot.repository.BookRepository;
import springboot.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List findAll() {
        return List.of(bookRepository.findAll());
    }
}
