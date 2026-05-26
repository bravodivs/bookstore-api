package api.bookstore.catalog_service.controllers;

import api.bookstore.catalog_service.models.PublisherDTO;
import api.bookstore.catalog_service.service.PublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalog/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService){
        this.publisherService = publisherService;
    }

    // POST /api/catalog/publishers
    @PostMapping
    public ResponseEntity<PublisherDTO> postPublishers(@RequestBody PublisherDTO publisher) {
        return ResponseEntity.ok(publisherService.createPublisher(publisher));
    }

    // GET /api/catalog/publishers
    @GetMapping
    public ResponseEntity<List<PublisherDTO>> getPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    // GET /api/catalog/publishers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisher(@PathVariable UUID id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO> updatePublisher(@PathVariable UUID id, @RequestBody PublisherDTO publisher){
        return ResponseEntity.ok(publisherService.updatePublisher(id, publisher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable UUID id){
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}
