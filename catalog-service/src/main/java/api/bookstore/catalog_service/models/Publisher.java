package api.bookstore.catalog_service.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "publishers")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String address;

    // One publisher -> many books
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books = new HashSet<>();

    public Publisher(UUID id, String name, String address, Set<Book> books) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.books = books;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Set<Book> getBooks() {
        return books;
    }

    // Getters & Setters
}
