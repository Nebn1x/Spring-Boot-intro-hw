package springboot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import springboot.dto.book.BookDtoWithoutCategoryIds;
import springboot.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<BookDtoWithoutCategoryIds> findAllByCategories_Id(Long categoryId);
}
