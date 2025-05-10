package springboot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springboot.dto.book.BookDto;
import springboot.dto.book.BookDtoWithoutCategoryIds;
import springboot.dto.book.CreateBookRequestDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.mapper.BookMapper;
import springboot.model.Book;
import springboot.model.Category;
import springboot.repository.BookRepository;
import springboot.repository.CategoryRepository;
import springboot.service.impl.BookServiceImpl;
import springboot.testutil.BookUtil;
import springboot.testutil.CategoryUtil;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private Category category;
    private BookDto bookDto;
    private CreateBookRequestDto requestDto;
    private Pageable pageable;
    private Page<Book> bookPage;

    @BeforeEach
    public void setUp() {
        requestDto = BookUtil.createBookRequestDto();
        book = BookUtil.getBook(requestDto);
        category = CategoryUtil.createCategory();
        bookDto = BookUtil.createBookDto(book);
        pageable = BookUtil.createPageable();
        bookPage = BookUtil.createBookPage(pageable);
    }

    @DisplayName("Create a book and return BookDto")
    @Test
    public void createBook_WithValidData_ReturnsBookDto() {
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        BookDto saveBook = bookService.createBook(requestDto);

        assertThat(saveBook).isEqualTo(bookDto);
        assertThat(saveBook).isNotNull();
        verify(bookRepository, times(1)).save(book);
    }

    @DisplayName("Throws NullPointerException when has is null")
    @Test
    public void createBook_WithNull_ThrowsException() {
        String exceptionMessage = "Book is null";
        CreateBookRequestDto requestDto = new CreateBookRequestDto();

        when(bookMapper.toModel(requestDto)).thenThrow(new NullPointerException(exceptionMessage));
        Exception exception = assertThrows(NullPointerException.class, () ->
                bookService.createBook(requestDto));

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @DisplayName("Create book with invalid category id throws EntityNotFoundException")
    @Test
    void createBook_WithInvalidCategoryId_ThrowException() {
        Long invalidCategoryId = 99L;
        String exceptionMessage = "Category not found with id: ";

        requestDto.setCategoryIds(List.of(invalidCategoryId));

        when(categoryRepository.findById(invalidCategoryId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                bookService.createBook(requestDto)
        );
        assertThat(exception.getMessage()).isEqualTo(exceptionMessage + invalidCategoryId);
    }

    @DisplayName("Return sorted list of BookDto")
    @Test
    void findAll_ReturnBookDtoPage() {
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);
        Page<BookDto> result = bookService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(bookDto);
    }

    @DisplayName("Return BookDto when book exists")
    @Test
    void getBookById_WithExistingId_ReturnBookDto() {
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getBookById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(bookDto);
    }

    @DisplayName("Should throw EntityNotFoundException when book does not exist")
    @Test
    void getBookById_WithNonExistingId_ThrowException() {
        String exceptionMs = "Cannot find Book with id: ";
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(id));

        assertThat(exception.getMessage()).isEqualTo(exceptionMs + id);
    }

    @Test
    @DisplayName("Update book and return updated BookDto when book exists")
    void updateBook_WithValidId_ReturnUpdatedBookDto() {
        Long bookId = 1L;

        CreateBookRequestDto updateBookDto = BookUtil.createBookRequestDto();
        updateBookDto.setTitle("Updated Title");
        updateBookDto.setAuthor("Updated Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateBookFromDto(updateBookDto, book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(
                new BookDto() {{
                    setId(bookId);
                    setTitle("Updated Title");
                    setAuthor("Updated Author");
                }}
        );

        BookDto result = bookService.updateBookById(updateBookDto, bookId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookId);
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getAuthor()).isEqualTo("Updated Author");
    }

    @Test
    @DisplayName("Throw EntityNotFoundException when book does not exist")
    void updateBook_WithInvalidId_ThrowException() {
        Long bookId = 99L;
        CreateBookRequestDto updateBookDto = BookUtil.createBookRequestDto();
        updateBookDto.setTitle("Updated Title");
        updateBookDto.setAuthor("Updated Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBookById(updateBookDto, bookId));

        assertThat(exception.getMessage()).isEqualTo("Cannot find Book with id: " + bookId);
    }

    @Test
    @DisplayName("Delete book when book exists")
    void deleteById_WithValidId_DeleteBook() {
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(bookId);

        bookService.deleteBookById(bookId);
    }

    @Test
    @DisplayName("Throw EntityNotFoundException when book does not exist")
    void deleteById_WithInvalidId_ThrowException() {
        Long bookId = 99L;
        String expectedMessage = "Cannot find Book with id: " + bookId;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteBookById(bookId));

        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @DisplayName("Return list of BookDtoWithoutCategoryIds by category id")
    @Test
    void getBooksByCategoryId_WithValidId_ReturnListOfDto() {
        Long categoryId = 1L;

        List<Book> books = BookUtil.getExpectedBooks();

        List<BookDtoWithoutCategoryIds> expectedDtos = BookUtil.getBookDtoWithoutCategoryIds();

        when(bookRepository.findByCategories_Id(categoryId)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(books.get(0))).thenReturn(expectedDtos.get(0));
        when(bookMapper.toDtoWithoutCategories(books.get(1))).thenReturn(expectedDtos.get(1));

        List<BookDtoWithoutCategoryIds> actualDto = bookService.getBooksByCategoryId(categoryId);

        assertThat(actualDto)
                .isNotNull()
                .hasSize(2)
                .containsExactly(expectedDtos.get(0), expectedDtos.get(1));
    }
}
