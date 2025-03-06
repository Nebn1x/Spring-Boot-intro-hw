package springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springboot.dto.book.BookDto;
import springboot.dto.book.BookDtoWithoutCategoryIds;
import springboot.dto.book.CreateBookRequestDto;
import springboot.service.BookService;

@Tag(name = "Books", description = "Operations related to books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Operation(
            summary = "Get all books",
            description = "Return list of all books. Accessible to USER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Books retrieved successfully"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Page<BookDto> getAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @Operation(
            summary = "Get a book by ID",
            description = "Return a book by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Book retrieved successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Book not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(
            summary = "Create a new book",
            description = "Creates a new book. Only available to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book created successfully"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public BookDto createBook(
            @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.createBook(requestDto);
    }

    @Operation(
            summary = "Update a book by ID",
            description = "Updates a book with the given ID. Only available to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Book updated successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Book not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public BookDto updateBookById(@RequestBody @Valid CreateBookRequestDto requestDto,
            @PathVariable Long id) {
        return bookService.updateBookById(requestDto, id);
    }

    @Operation(
            summary = "Delete a book by ID",
            description = "Deletes a book by its ID. Only available to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Book not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @Operation(
            summary = "Get books by category ID",
            description = "Return all books associated with the specified category.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Books retrieved successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Category not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.getBooksByCategoryId(id);
    }
}
