package api.bookstore.catalog_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/publishers")
public class PublisherController {

    // POST /api/catalog/publishers
    @PostMapping
    public ResponseEntity<String> postPublishers(@RequestBody String publisher) {
        // TODO: save publisher
        return ResponseEntity.ok("Publisher created successfully");
    }

    // GET /api/catalog/publishers
    @GetMapping
    public ResponseEntity<List<String>> getPublishers() {
        // TODO: return list of publishers
        return ResponseEntity.ok(List.of("Publisher1", "Publisher2"));
    }

    // GET /api/catalog/publishers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<String> getPublisher(@PathVariable String id) {
        // TODO: return publisher details by id
        return ResponseEntity.ok("Details of publisher " + id);
    }
}
