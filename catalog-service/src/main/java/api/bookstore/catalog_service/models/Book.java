package api.bookstore.catalog_service.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String title;

    private String description;

    private String language;

    private LocalDate publicationDate;

    private BigDecimal price;

    // Many books -> one publisher
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "publisher_id", nullable = true)
    private Publisher publisher;

    // Many books <-> many authors
    @ManyToMany
    @JoinTable(
            name = "book_authors", // 👈 join table name
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", publicationDate=" + publicationDate +
                ", price=" + price +
                ", publisher=" + publisher +
                ", authors=" + authors +
                '}';
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    // Getters

}
