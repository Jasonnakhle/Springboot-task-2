
package library_management.Controller;

import library_management.DTO.AuthorDTO;
import library_management.DTO.AuthorMapper;
import library_management.DTO.BookDTO;
import library_management.DTO.BookMapper;
import library_management.Entity.Author;
import library_management.Service.AuthorService;
import library_management.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService,
                            BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @PostMapping
    public AuthorDTO addAuthor(@Valid @RequestBody AuthorDTO dto) {
        Author author = AuthorMapper.toEntity(dto);
        return AuthorMapper.toDTO(authorService.addAuthor(author));
    }

    @GetMapping
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors()
                .stream()
                .map(AuthorMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public AuthorDTO getAuthorById(@PathVariable Long id) {
        return AuthorMapper.toDTO(authorService.getAuthorById(id));
    }

    @PutMapping("/{id}")
    public AuthorDTO updateAuthor(@PathVariable Long id,
                                  @Valid @RequestBody AuthorDTO dto) {
        Author author = AuthorMapper.toEntity(dto);
        return AuthorMapper.toDTO(authorService.updateAuthor(id, author));
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping("/{id}/books")
    public List<BookDTO> getBooksByAuthor(@PathVariable Long id) {
        return bookService.getBooksByAuthorId(id)
                .stream()
                .map(BookMapper::toDTO)
                .toList();
    }
}
