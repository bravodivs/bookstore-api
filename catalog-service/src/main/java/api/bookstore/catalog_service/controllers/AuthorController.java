package api.bookstore.catalog_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/authors")
public class AuthorController {

    // POST /api/catalog/authors
    @PostMapping
    public ResponseEntity<String> postAuthors(@RequestBody String author) {
        // TODO: save author
        return ResponseEntity.ok("Author created successfully");
    }

    // GET /api/catalog/authors
    @GetMapping
    public ResponseEntity<List<String>> getAuthors() {
        // TODO: return list of authors
        return ResponseEntity.ok(List.of("Author1", "Author2"));
    }

    // GET /api/catalog/authors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<String> getAuthor(@PathVariable String id) {
        // TODO: return author details by id
        return ResponseEntity.ok("Details of author " + id);
    }
}
