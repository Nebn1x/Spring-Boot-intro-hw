package springboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springboot.dto.BookDto;
import springboot.dto.CreateBookRequestDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.mapper.BookMapper;
import springboot.model.Book;
import springboot.repository.BookRepository;
import springboot.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
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
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }
        throw new EntityNotFoundException("Cannot find Book with id: " + id);
    }
}
