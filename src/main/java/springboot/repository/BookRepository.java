package springboot.repository;

import springboot.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List findAll();
}
