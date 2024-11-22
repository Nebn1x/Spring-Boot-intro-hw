package springboot.service;

import java.util.List;
import springboot.model.Book;

public interface BookService {
    Book save(Book book);

    List findAll();
}
