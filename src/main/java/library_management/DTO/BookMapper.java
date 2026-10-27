
package library_management.DTO;

import library_management.Entity.Book;

public class BookMapper {

    public static BookDTO toDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getCategory() != null
                        ? book.getCategory().name()
                        : null,
                book.isAvailable(),
                book.getAuthor() != null
                        ? book.getAuthor().getId()
                        : null
        );
    }
}
