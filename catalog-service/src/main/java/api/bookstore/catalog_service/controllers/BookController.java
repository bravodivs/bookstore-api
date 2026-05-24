package api.bookstore.catalog_service.controllers;

import api.bookstore.catalog_service.models.Book;
import api.bookstore.catalog_service.models.BookDTO;
import api.bookstore.catalog_service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalog/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // POST /api/catalog/books
    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody BookDTO book) {
        // TODO: save book
        var id = bookService.createBook(book);
        return ResponseEntity.ok("Book created successfully with id " + id);
    }

    // GET /api/catalog/books
    @GetMapping
    public ResponseEntity<List<BookDTO>> getBooks() {
        // TODO: return list of books
        var books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // GET /api/catalog/books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookDetails(@PathVariable UUID id) {
        var book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    // GET /api/catalog/books/search?title=foo
    @GetMapping("/search")
    public ResponseEntity<List<Book>> getBooksByTitle(@RequestParam String title) {
        var books = bookService.search(title);
        return ResponseEntity.ok(books);
    }

    // PUT /api/catalog/books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable String id, @RequestBody String book) {
        // TODO: update book
        return ResponseEntity.ok("Book " + id + " updated successfully");
    }

    // DELETE /api/catalog/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable UUID id) {
        // TODO: delete book
        if(bookService.deleteBook(id))
            return ResponseEntity.ok("Book " + id + " deleted successfully");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Book " + id + " not found");
    }
}
