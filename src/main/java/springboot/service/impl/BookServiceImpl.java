package springboot.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springboot.dto.book.BookDto;
import springboot.dto.book.BookDtoWithoutCategoryIds;
import springboot.dto.book.CreateBookRequestDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.mapper.BookMapper;
import springboot.model.Book;
import springboot.model.Category;
import springboot.repository.BookRepository;
import springboot.repository.CategoryRepository;
import springboot.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto createBook(CreateBookRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        Book book = bookMapper.toModel(requestDto);

        Set<Category> categories = requestDto.getCategoryIds().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Category not found with id: " + id)))
                .collect(Collectors.toSet());

        book.setCategories(categories);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
               .map(bookMapper::toDto)
               .orElseThrow(() -> new EntityNotFoundException("Cannot find Book with id: " + id));
    }

    @Override
    public BookDto updateBookById(CreateBookRequestDto requestDto, Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find Book with id: " + id));
        bookMapper.updateBookFromDto(requestDto, book);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot find Book with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Cannot find Books by category id: " + categoryId);
        }
        return bookRepository.findByCategories_Id(categoryId)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
