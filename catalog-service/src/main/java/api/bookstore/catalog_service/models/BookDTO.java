package api.bookstore.catalog_service.models;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Book data transfer object for create and list operations")
public class BookDTO {

    @Schema(description = "Book identifier (generated on create)", accessMode = Schema.AccessMode.READ_ONLY,
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Book title", example = "Spring Microservices in Action", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "International Standard Book Number", example = "978-1617294847",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String isbn;

    @Schema(description = "Short description of the book", example = "A practical guide to microservices")
    private String description;

    @Schema(description = "List price", example = "49.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "Publication date", example = "2024-03-15")
    private LocalDate publicationDate;

    @Schema(description = "Publisher details")
    private PublisherDTO publisher;

    @Schema(description = "Authors of the book")
    private Set<AuthorDTO> authors;

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", publicationDate=" + publicationDate +
                ", publisher=" + publisher +
                ", authors=" + authors +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public PublisherDTO getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherDTO publisher) {
        this.publisher = publisher;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }
    // getters and setters

}
