package api.bookstore.catalog_service.controllers;

import api.bookstore.catalog_service.models.Book;
import api.bookstore.catalog_service.models.BookDTO;
import api.bookstore.catalog_service.service.BookService;
import api.bookstore.catalog_service.utils.BookUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Books", description = "Create, read, update, and delete books in the catalog")
@RestController
@RequestMapping("/api/catalog/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Create a book", description = "Adds a new book to the catalog and publishes a book.created event")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book created",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> createBook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Book to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BookDTO.class)))
            @RequestBody BookDTO book) {
        var id = bookService.createBook(book);
        return ResponseEntity.ok("Book created successfully with id " + id);
    }

    @Operation(summary = "List all books", description = "Returns every book in the catalog as DTOs")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books found"),
            @ApiResponse(responseCode = "404", description = "No books in the database", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<BookDTO>> getBooks() {
        var books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get book by ID", description = "Returns full book details including publisher and authors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookDetails(
            @Parameter(description = "Book UUID", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @PathVariable UUID id) {
        var book = bookService.getBookById(id);
        return ResponseEntity.ok(BookUtil.BookDaoToDto(book));
    }

    @Operation(summary = "Search books by title", description = "Case-insensitive partial match on book title")
    @ApiResponse(responseCode = "200", description = "Matching books (may be empty)")
    @GetMapping("/search")
    public ResponseEntity<api.bookstore.catalog_service.models.ApiResponse<List<BookDTO>>> getBooksByTitle(
            @Parameter(description = "Title search term", required = true, example = "Spring")
            @RequestParam String title) {
        List<BookDTO> books = bookService.search(title);
        if (books.isEmpty())
            return ResponseEntity.ok(
                    new api.bookstore.catalog_service.models.ApiResponse<>("No books found", books)
            );
        return ResponseEntity.ok(
                new api.bookstore.catalog_service.models.ApiResponse<>("Books fetched", books)
        );
    }

    @Operation(summary = "Update a book", description = "Updates an existing book (placeholder implementation)")
    @ApiResponse(responseCode = "200", description = "Book updated")
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @Parameter(description = "Book ID", required = true) @PathVariable UUID id,
            @RequestBody BookDTO book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @Operation(summary = "Delete a book", description = "Removes a book from the catalog by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book deleted"),
            @ApiResponse(responseCode = "400", description = "Book not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book UUID", required = true) @PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
