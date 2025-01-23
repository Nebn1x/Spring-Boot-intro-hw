package springboot.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springboot.dto.BookDto;
import springboot.dto.CreateBookRequestDto;
import springboot.service.BookService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;


    @GetMapping
    public Page<BookDto> getAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.createBook(requestDto);
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/{id}")
    public BookDto updateBookById(@RequestBody @Valid CreateBookRequestDto requestDto,
                                  @PathVariable Long id) {
        return bookService.updateBookById(requestDto, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }
}
