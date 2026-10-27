
package library_management.Service;

import library_management.Entity.Book;
import library_management.Repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final Logger logger =
            LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        logger.info("Adding book: {}", book.getTitle());

        Book savedBook = bookRepository.save(book);

        logger.info("Book added successfully with ID: {}",
                savedBook.getId());

        return savedBook;
    }

    public List<Book> getAllBooks() {
        logger.info("Retrieving all books");
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        logger.info("Retrieving book with ID: {}", id);

        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Book not found with ID: {}", id);
                    return new RuntimeException("Book not found");
                });
    }

    public void deleteBook(Long id) {
        logger.info("Deleting book with ID: {}", id);

        bookRepository.deleteById(id);

        logger.info("Book deleted successfully with ID: {}", id);
    }

    public Book updateBook(Long id, Book updatedBook) {
        logger.info("Updating book with ID: {}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Book not found for update with ID: {}", id);
                    return new RuntimeException("Book not found");
                });

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setAvailable(updatedBook.isAvailable());

        Book savedBook = bookRepository.save(existingBook);

        logger.info("Book updated successfully with ID: {}", id);

        return savedBook;
    }

    public List<Book> getBooksByAuthorId(Long authorId) {
        logger.info("Retrieving books for author ID: {}", authorId);
        return bookRepository.findByAuthorId(authorId);
    }
}
