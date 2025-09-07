package api.bookstore.catalog_service.models;

public class BookDTO {
        private Long id;
        private String title;
        private Double price;

        private PublisherDTO publisher;
        private Set<AuthorDTO> authors;

        // getters and setters

}
