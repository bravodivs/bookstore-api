package api.bookstore.catalog_service.models;

import java.util.Set;
import java.util.UUID;

public class AuthorDTO {
    private UUID id;
    private String name;
    private String bio;

    public AuthorDTO(UUID id, String name, String bio) {
        this.id = id;
        this.name = name;
        this.bio = bio;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    // no list of books here (to prevent recursion)
}
