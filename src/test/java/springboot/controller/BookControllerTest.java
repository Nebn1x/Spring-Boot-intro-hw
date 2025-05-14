package springboot.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static springboot.testutil.BookUtil.createBookDto;
import static springboot.testutil.BookUtil.createBookRequestDto;
import static springboot.testutil.BookUtil.createUpdateBookRequestDto;
import static springboot.testutil.BookUtil.getBook;
import static springboot.testutil.BookUtil.getBookDto;
import static springboot.testutil.BookUtil.getBookDtoWithoutCategoryIds;
import static springboot.testutil.BookUtil.getBooks;
import static springboot.testutil.BookUtil.getUpdateBookDto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import springboot.dto.book.BookDto;
import springboot.dto.book.BookDtoWithoutCategoryIds;
import springboot.dto.book.CreateBookRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    private static final String INSERT_BOOKS_SCRIPT_PATH =
            "database/books/insert-books-with-categories-to-db.sql";
    private static final String INSERT_CATEGORIES_SCRIPT_PATH =
            "database/categories/insert-categories-to-test-db.sql";
    private static final String REMOVE_ALL_SCRIPT_PATH =
            "database/delete-all-data.sql";
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_CATEGORIES_SCRIPT_PATH));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_BOOKS_SCRIPT_PATH));
        }
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Create a book with valid parameters")
    void createBook_ValidData_Success() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto();
        BookDto expected = createBookDto(getBook(requestDto));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Create book invalid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void createBook_InvalidData_ReturnException() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DisplayName("Get all books by existing data")
    void getAll_ValidData_ReturnPageBookDto() throws Exception {
        Pageable requestDto = Pageable.ofSize(3);
        List<BookDto> expectedBooks = getBooks();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "3")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<BookDto> actualBooks = objectMapper.readValue(
                root.get("content").toString(),
                new TypeReference<>() {
                }
        );
        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @Test
    @DisplayName("Get book by valid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getBook_ByValidId_ReturnBookDto() throws Exception {
        Long bookId = 101L;
        BookDto expectedDto = getBookDto(bookId);

        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualDto = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), BookDto.class);

        assertNotNull(actualDto);
        assertNotNull(actualDto.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto));
    }

    @Test
    @DisplayName("Get book with invalid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getBook_ByInvalidId_ReturnNotFound() throws Exception {
        Long bookId = 999L;

        mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update book with valid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateBook_ValidData_ReturnUpdatedBookDto() throws Exception {
        Long bookId = 101L;

        BookDto expectedDto = getUpdateBookDto(bookId);
        CreateBookRequestDto requestDto = createUpdateBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put("/books/{id}", bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualDto = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), BookDto.class);

        assertNotNull(actualDto);
        assertNotNull(actualDto.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto));
    }

    @Test
    @DisplayName("Update book with invalid id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateBook_InvalidId_ReturnNotFound() throws Exception {
        Long bookId = 999L;

        CreateBookRequestDto requestDto = createBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/books/{id}", bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete book by valid id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteBook_ById_ReturnNothing() throws Exception {
        Long bookId = 101L;

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Delete book with invalid id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteBook_InvalidId_ReturnNotFound() throws Exception {
        Long bookId = 999L;

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get books by invalid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getBooksByCategoryId_InvalidId_ReturnNotFound() throws Exception {
        Long categoryId = 999L;

        mockMvc.perform(get("/books/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get books by category id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getBooksByCategoryId_ValidCategoryId_ReturnBooks() throws Exception {
        Long categoryId = 101L;

        List<BookDtoWithoutCategoryIds> expectedDtos = getBookDtoWithoutCategoryIds();
        MvcResult result = mockMvc.perform(get("/books/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> actualDtos = objectMapper
                .readValue(result.getResponse()
                        .getContentAsString(), objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, BookDtoWithoutCategoryIds.class));
        assertThat(actualDtos).usingRecursiveComparison().isEqualTo(expectedDtos);
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(REMOVE_ALL_SCRIPT_PATH));
        }
    }
}
