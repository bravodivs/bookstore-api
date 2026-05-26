package api.bookstore.catalog_service.controllers;

import api.bookstore.catalog_service.models.AuthorDTO;
import api.bookstore.catalog_service.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalog/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService){
        this.authorService= authorService;
    }

    // POST /api/catalog/authors
    @PostMapping
    public ResponseEntity<AuthorDTO> postAuthors(@RequestBody AuthorDTO author) {
        return ResponseEntity.ok(authorService.createAuthor(author));
    }

    // GET /api/catalog/authors
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    // GET /api/catalog/authors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable UUID id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable UUID id, @RequestBody AuthorDTO author){
        return ResponseEntity.ok(authorService.updateAuthor(id, author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id){
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
