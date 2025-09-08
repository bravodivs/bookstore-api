package api.bookstore.catalog_service.models;

import java.util.Set;
import java.util.UUID;

public class PublisherDTO {
    private UUID id;
    private String name;
    private String address;
    private Set<Book> books;

    public PublisherDTO(UUID id, String name, String address, Set<Book> books) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.books = books;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    // no list of books here (to prevent recursion)
    // getters and setters
}
