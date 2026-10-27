
package library_management.Service;

import library_management.Entity.Author;
import library_management.Repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author addAuthor(Author author) {
        logger.info("Adding author: {}", author.getName());

        Author savedAuthor = authorRepository.save(author);

        logger.info("Author added successfully with ID: {}",
                savedAuthor.getId());

        return savedAuthor;
    }

    public List<Author> getAllAuthors() {
        logger.info("Retrieving all authors");
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        logger.info("Retrieving author with ID: {}", id);

        return authorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Author not found with ID: {}", id);
                    return new RuntimeException("Author not found");
                });
    }

    public void deleteAuthor(Long id) {
        logger.info("Deleting author with ID: {}", id);

        authorRepository.deleteById(id);

        logger.info("Author deleted successfully with ID: {}", id);
    }

    public Author updateAuthor(Long id, Author updatedAuthor) {
        logger.info("Updating author with ID: {}", id);

        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Author not found for update with ID: {}", id);
                    return new RuntimeException("Author not found");
                });

        existingAuthor.setName(updatedAuthor.getName());
        existingAuthor.setBiography(updatedAuthor.getBiography());

        Author savedAuthor = authorRepository.save(existingAuthor);

        logger.info("Author updated successfully with ID: {}", id);

        return savedAuthor;
    }
}
