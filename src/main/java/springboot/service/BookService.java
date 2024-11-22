package springboot.service;

import springboot.model.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);

    List findAll();
}
