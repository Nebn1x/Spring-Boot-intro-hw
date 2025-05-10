package springboot.testutil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import springboot.dto.book.BookDto;
import springboot.dto.book.BookDtoWithoutCategoryIds;
import springboot.dto.book.CreateBookRequestDto;
import springboot.model.Book;
import springboot.model.Category;

public class BookUtil {

    public static Book getBook(CreateBookRequestDto requestDto) {
        Book book = new Book();
        book.setId(1L);
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());

        Category category = new Category();
        category.setId(1L);
        category.setName("1");
        category.setDescription("Description");
        category.setDeleted(false);
        book.setCategories(Set.of(category));

        return book;
    }

    public static BookDto createBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoryIds(List.of(1L));

        return bookDto;
    }

    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Title");
        requestDto.setAuthor("Author");
        requestDto.setIsbn("Isbn");
        requestDto.setPrice(BigDecimal.valueOf(150));
        requestDto.setDescription("Description");
        requestDto.setCoverImage("CoverImage");

        Category category = new Category();
        category.setId(101L);
        category.setName("1");
        category.setDescription("Description");
        category.setDeleted(false);
        requestDto.setCategoryIds(List.of(1L));
        requestDto.setCategoryIds(List.of(category.getId()));

        return requestDto;
    }

    public static Pageable createPageable() {
        return PageRequest.of(0, 10, Sort.by("title"));
    }

    public static Page<Book> createBookPage(Pageable pageable) {
        Book book = getBook(createBookRequestDto());
        return new PageImpl<>(List.of(book), pageable, 1);
    }

    public static List<Book> getExpectedBooks() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title1");
        book1.setAuthor("Author1");
        book1.setIsbn("Isbn1");
        book1.setPrice(BigDecimal.valueOf(100));
        book1.setDescription("Description1");
        book1.setCoverImage("CoverImage1");
        book1.setDeleted(false);
        book1.setCategories(Set.of(CategoryUtil.createCategory()));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title2");
        book2.setAuthor("Author2");
        book2.setIsbn("Isbn2");
        book2.setPrice(BigDecimal.valueOf(200));
        book2.setDescription("Description2");
        book2.setCoverImage("CoverImage2");
        book2.setDeleted(false);
        book2.setCategories(Set.of(CategoryUtil.createCategory()));

        return List.of(book1, book2);
    }

    public static BookDto getBookDto(Long testId) {
        String testTitle = "NewBookTitle1";
        String testAuthor = "NewBookAuthor1";
        String testIsbn = "NewBookIsbn1";
        String testDescription = "NewBookDescription1";
        String testCoverImage = "NewCoverImage1";
        List<Long> testCategoryIds = List.of(1L);
        BigDecimal testPrice = BigDecimal.valueOf(150);

        BookDto expectedDto = new BookDto();
        expectedDto.setId(testId);
        expectedDto.setTitle(testTitle);
        expectedDto.setAuthor(testAuthor);
        expectedDto.setIsbn(testIsbn);
        expectedDto.setDescription(testDescription);
        expectedDto.setCoverImage(testCoverImage);
        expectedDto.setCategoryIds(testCategoryIds);
        expectedDto.setPrice(testPrice);
        return expectedDto;
    }

    public static List<BookDto> getBooks() {
        BookDto book1 = new BookDto();
        book1.setId(101L);
        book1.setTitle("Title1");
        book1.setAuthor("Author1");
        book1.setIsbn("Isbn1");
        book1.setPrice(BigDecimal.valueOf(100));
        book1.setDescription("Description1");
        book1.setCoverImage("CoverImage1");
        book1.setCategoryIds(List.of(1L));

        BookDto book2 = new BookDto();
        book2.setId(102L);
        book2.setTitle("Title2");
        book2.setAuthor("Author2");
        book2.setIsbn("Isbn2");
        book2.setPrice(BigDecimal.valueOf(200));
        book2.setDescription("Description2");
        book2.setCoverImage("CoverImage2");
        book2.setCategoryIds(List.of(1L));

        BookDto book3 = new BookDto();
        book3.setId(103L);
        book3.setTitle("Title3");
        book3.setAuthor("Author3");
        book3.setIsbn("Isbn3");
        book3.setPrice(BigDecimal.valueOf(300));
        book3.setDescription("Description3");
        book3.setCoverImage("CoverImage3");
        book3.setCategoryIds(List.of(2L));

        return List.of(book1, book2, book3);
    }

    public static List<BookDtoWithoutCategoryIds> getBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds1
                = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds1.setId(1L);
        bookDtoWithoutCategoryIds1.setTitle("Title1");
        bookDtoWithoutCategoryIds1.setAuthor("Author1");
        bookDtoWithoutCategoryIds1.setIsbn("Isbn1");
        bookDtoWithoutCategoryIds1.setPrice(BigDecimal.valueOf(100));
        bookDtoWithoutCategoryIds1.setDescription("Description1");
        bookDtoWithoutCategoryIds1.setCoverImage("CoverImage1");

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds2
                = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds2.setId(2L);
        bookDtoWithoutCategoryIds2.setTitle("Title2");
        bookDtoWithoutCategoryIds2.setAuthor("Author2");
        bookDtoWithoutCategoryIds2.setIsbn("Isbn2");
        bookDtoWithoutCategoryIds2.setPrice(BigDecimal.valueOf(200));
        bookDtoWithoutCategoryIds2.setDescription("Description2");
        bookDtoWithoutCategoryIds2.setCoverImage("CoverImage2");

        return List.of(bookDtoWithoutCategoryIds1, bookDtoWithoutCategoryIds2);
    }
}
