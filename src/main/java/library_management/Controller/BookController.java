
package library_management.Controller;

import library_management.DTO.BookDTO;
import library_management.DTO.BookMapper;
import library_management.Entity.Book;
import library_management.Entity.Category;
import library_management.Service.AuthorService;
import library_management.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService,
                          AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @PostMapping
    public BookDTO addBook(@Valid @RequestBody BookDTO dto) {
        Book book = new Book();

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setCategory(Category.valueOf(dto.getCategory()));

        book.setAvailable(
                dto.getAvailable() == null || dto.getAvailable()
        );

        return BookMapper.toDTO(bookService.addBook(book));
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks()
                .stream()
                .map(BookMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return BookMapper.toDTO(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    public BookDTO updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO dto) {

        Book book = new Book();

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setCategory(Category.valueOf(dto.getCategory()));

        book.setAvailable(
                dto.getAvailable() == null || dto.getAvailable()
        );

        if (dto.getAuthorId() != null) {
            book.setAuthor(
                    authorService.getAuthorById(dto.getAuthorId())
            );
        }

        return BookMapper.toDTO(bookService.updateBook(id, book));
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
