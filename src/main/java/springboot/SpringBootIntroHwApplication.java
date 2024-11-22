package springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springboot.model.Book;
import springboot.service.BookService;

@SpringBootApplication
public class SpringBootIntroHwApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootIntroHwApplication.class, args);

    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Java Spring Boot");
            book.setAuthor("James Bond");
            bookService.save(book);
        };
    }
}
