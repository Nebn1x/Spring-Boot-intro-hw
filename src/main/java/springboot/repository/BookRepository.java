package springboot.repository;

import java.util.List;
import springboot.model.Book;

public interface BookRepository {
    Book save(Book book);

    List findAll();
}
