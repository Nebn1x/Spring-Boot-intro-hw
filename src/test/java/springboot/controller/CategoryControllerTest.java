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
import static springboot.testutil.CategoryUtil.createCategoryDto;
import static springboot.testutil.CategoryUtil.createCategoryRequestDto;
import static springboot.testutil.CategoryUtil.createNewCategoryRequestDto;
import static springboot.testutil.CategoryUtil.getCategories;
import static springboot.testutil.CategoryUtil.getCategory;
import static springboot.testutil.CategoryUtil.getCategoryDto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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
import springboot.dto.category.CategoryDto;
import springboot.dto.category.CreateCategoryRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
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
        teardown(dataSource);
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
    @DisplayName("Create a category with valid parameters")
    void createCategory_ValidData_Success() throws Exception {
        CreateCategoryRequestDto requestDto = createNewCategoryRequestDto();
        CategoryDto expected = createCategoryDto(getCategory(requestDto));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Create category with null")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void createCategory_WithNull_ReturnException() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DisplayName("Get all categories by existing data")
    void getAll_ValidData_ReturnPageCategoryDto() throws Exception {
        Pageable requestDto = Pageable.ofSize(3);
        List<CategoryDto> expectedCategories = getCategories();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "3")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<CategoryDto> actualCategories = objectMapper.readValue(
                root.get("content").toString(),
                new TypeReference<>() {
                }
        );
        assertNotNull(expectedCategories);
        assertThat(actualCategories).usingRecursiveComparison().isEqualTo(expectedCategories);
    }

    @Test
    @DisplayName("Get category by valid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getCategory_ByValidId_ReturnCategoryDto() throws Exception {
        Long categoryId = 101L;
        CategoryDto expectedDto = getCategoryDto(categoryId);

        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actualDto = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(actualDto);
        assertNotNull(actualDto.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto));
    }

    @Test
    @DisplayName("Get category with invalid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getCategory_ByInvalidId_ReturnNotFound() throws Exception {
        Long categoryId = 999L;

        mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update category with valid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateCategory_ValidData_ReturnCategoryDto() throws Exception {
        Long categoryId = 101L;

        CategoryDto expectedDto = getCategoryDto(categoryId);
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actualDto = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(actualDto);
        assertNotNull(actualDto.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto));
    }

    @Test
    @DisplayName("Update category with invalid id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateCategory_InvalidId_ReturnNotFound() throws Exception {
        Long categoryId = 999L;

        CreateCategoryRequestDto requestDto = createCategoryRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete category by valid id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteCategory_ById_ReturnNothing() throws Exception {
        Long categoryId = 101L;

        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Delete book with invalid id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteCategory_InvalidId_ReturnNotFound() throws Exception {
        Long categoryId = 999L;

        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
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
