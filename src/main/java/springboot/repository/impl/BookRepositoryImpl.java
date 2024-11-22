package springboot.repository.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import springboot.exeptions.DataProcessingException;
import springboot.model.Book;
import springboot.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final SessionFactory sessionFactory;
    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
    Session session = null;
    Transaction transaction = null;
    try {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.persist(book);
        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        throw new DataProcessingException("Can't add book :" + book);
    } finally {
        if (session != null) {
            session.close();
        }
        return book;
    }
}

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> getAllBookQuery = session.createQuery(
                    "from Book", Book.class);
            return getAllBookQuery.getResultList();
        }
    }
}