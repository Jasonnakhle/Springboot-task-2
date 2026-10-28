package library_management.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class OpenLibraryService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenLibraryService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://openlibrary.org")
                .build();
    }

    public String getAuthorNameByISBN(String isbn) {

        String response;

        try {
            response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/books")
                            .queryParam("bibkeys", "ISBN:" + isbn)
                            .queryParam("format", "json")
                            .queryParam("jscmd", "data")
                            .build())
                    .retrieve()
                    .body(String.class);

        } catch (org.springframework.web.client.RestClientException e) {
            throw new IllegalStateException(
                    "Unable to contact Open Library", e);
        }

        if (response == null || response.isBlank()) {
            return null;
        }

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode book = root.path("ISBN:" + isbn);
            JsonNode authors = book.path("authors");

            if (!authors.isArray() || authors.isEmpty()) {
                return null;
            }

            String authorName = authors.get(0).path("name").asText(null);

            if (authorName == null || authorName.isBlank()) {
                return null;
            }

            return authorName.trim();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to parse Open Library response", e);
        }
    }
}