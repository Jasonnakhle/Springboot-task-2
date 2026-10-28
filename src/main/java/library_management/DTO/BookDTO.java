
package library_management.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BookDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Category is required")
    @Pattern(
            regexp = "FICTION|NON_FICTION|SCIENCE|HISTORY|TECHNOLOGY|OTHER",
            message = "Category must be FICTION, NON_FICTION, SCIENCE, HISTORY, TECHNOLOGY, or OTHER"
    )
    private String category;

    private Boolean available;

    private Long authorId;

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String isbn,
                   String category, Boolean available, Long authorId) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.category = category;
        this.available = available;
        this.authorId = authorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
