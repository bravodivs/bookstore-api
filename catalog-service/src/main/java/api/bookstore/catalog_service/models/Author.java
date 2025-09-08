package api.bookstore.catalog_service.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String bio;

    // Many-to-many with books (inverse side)
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

    public Author(UUID id, String name, String bio, Set<Book> books) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.books = books;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public Set<Book> getBooks() {
        return books;
    }

    // Getters & Setters

}
