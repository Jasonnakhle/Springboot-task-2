
package library_management.DTO;

import library_management.Entity.Author;

public class AuthorMapper {

    public static AuthorDTO toDTO(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getBiography()
        );
    }

    public static Author toEntity(AuthorDTO dto) {
        Author author = new Author();
        author.setName(dto.getName());
        author.setBiography(dto.getBiography());
        return author;
    }
}
