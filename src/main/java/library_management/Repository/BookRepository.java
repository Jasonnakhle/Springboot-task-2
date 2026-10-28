
package library_management.Repository;

import library_management.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    List<Book> findByAuthorId(Long authorId);
}
