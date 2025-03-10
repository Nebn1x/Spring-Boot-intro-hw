package springboot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.dto.book.BookDtoWithoutCategoryIds;
import springboot.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<BookDtoWithoutCategoryIds> findAllByCategories_Id(Long categoryId);
}
