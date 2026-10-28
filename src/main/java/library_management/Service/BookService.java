
package library_management.Service;

import library_management.Entity.Book;
import library_management.Entity.Author;
import library_management.Repository.BookRepository;
import library_management.Repository.AuthorRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final Logger logger =
            LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final OpenLibraryService openLibraryService;

    public BookService(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            OpenLibraryService openLibraryService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.openLibraryService = openLibraryService;
    }

    public Book addBook(Book book) {
        logger.info("Adding book: {}", book.getTitle());

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException(
                    "A book with this ISBN already exists"
            );
        }

        String authorName =
                openLibraryService.getAuthorNameByISBN(book.getIsbn());

        logger.info("Author returned by Open Library: {}", authorName);

        if (authorName != null && !authorName.isBlank()) {
            Author author = authorRepository
                    .findByNameIgnoreCase(authorName)
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(authorName);
                        return authorRepository.save(newAuthor);
                    });

            book.setAuthor(author);
        }

        Book savedBook = bookRepository.save(book);

        logger.info(
                "Book added successfully with ID: {}",
                savedBook.getId()
        );

        return savedBook;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Book not found with ID: " + id
                        ));
    }

    public List<Book> getBooksByAuthorId(Long authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = getBookById(id);

        if (!existingBook.getIsbn().equals(updatedBook.getIsbn())
                && bookRepository.existsByIsbn(updatedBook.getIsbn())) {
            throw new IllegalArgumentException(
                    "A book with this ISBN already exists"
            );
        }

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setAvailable(updatedBook.isAvailable());

        if (updatedBook.getAuthor() != null) {
            existingBook.setAuthor(updatedBook.getAuthor());
        }

        Book savedBook = bookRepository.save(existingBook);

        logger.info(
                "Book updated successfully with ID: {}",
                savedBook.getId()
        );

        return savedBook;
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);

        logger.info("Book deleted successfully with ID: {}", id);
    }
}
