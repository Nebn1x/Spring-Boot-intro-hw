package springboot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import springboot.model.Book;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/categories/insert-categories-to-test-db.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/books/insert-books-with-categories-to-db.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/delete-all-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Should find books by existing category ID")
    void findByCategoriesId_shouldReturnBooksForCategory() {
        Long categoryId = 101L;

        List<Book> books = bookRepository.findByCategories_Id(categoryId);

        assertThat(books)
                .hasSize(2)
                .extracting(Book::getTitle)
                .containsExactlyInAnyOrder("Title1", "Title2");
    }

    @Test
    @DisplayName("Should return empty list for non-existing category ID")
    void findByCategoriesId_CategoryDoesNotExist_ReturnEmptyList() {
        Long categoryId = 999L;

        List<Book> actualBooks =
                bookRepository.findByCategories_Id(categoryId);

        assertThat(actualBooks).isEmpty();
    }
}
