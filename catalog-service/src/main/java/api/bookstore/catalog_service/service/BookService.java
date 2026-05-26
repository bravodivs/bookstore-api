package api.bookstore.catalog_service.service;

import api.bookstore.catalog_service.event.BookEventPublisher;
import api.bookstore.catalog_service.exception.CustomException;
import api.bookstore.catalog_service.models.Book;
import api.bookstore.catalog_service.models.BookDTO;
import api.bookstore.catalog_service.repository.BookRepository;
import api.bookstore.catalog_service.utils.BookUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookEventPublisher eventPublisher;

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public BookService(BookRepository bookRepository, BookEventPublisher eventPublisher){
        this.bookRepository = bookRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<BookDTO> getAllBooks(){
        return bookRepository.findAll().stream()
                .map(BookUtil::BookDaoToDto)
                .toList();
    }

    public UUID createBook(BookDTO book) {
        validateBookInput(book);
        Book saved = bookRepository.save(BookUtil.BookDtoToDao(book));
        try {
            eventPublisher.publishBookCreated(saved);
        }
        catch (Exception e){
            logger.error("Error publishing event. Check kafka is running. Error- {}", e.getMessage());
        }
        return saved.getId();
    }

    public Book getBookById(UUID id){
        var book = bookRepository.findById(id);
        if(book.isPresent())
            return book.get();
        logger.warn("No book found for given ID " + id);
        throw  new CustomException("No books found", HttpStatus.NOT_FOUND);
    }

    //todo handle not found case
    public List<BookDTO> search(String title) {
       return bookRepository.findByTitleContainingIgnoreCase(title).stream()
               .map(BookUtil::BookDaoToDto)
               .toList();
    }

    // todo add logic to delete by other params such as title
    public void deleteBook(UUID id) {
        if (!bookRepository.existsById(id)){
            throw new CustomException("Book not found", HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }

    public BookDTO updateBook(UUID id, BookDTO request){
        validateBookInput(request);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new CustomException("No book found", HttpStatus.NOT_FOUND));
        Book mappedRequest = BookUtil.BookDtoToDao(request);
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setPublicationDate(request.getPublicationDate());
        if(request.getPublisher() != null){
            book.setPublisher(mappedRequest.getPublisher());
        }
        if(request.getAuthors() != null){
            book.setAuthors(mappedRequest.getAuthors());
        }
        return BookUtil.BookDaoToDto(bookRepository.save(book));
    }

    private void validateBookInput(BookDTO book){
        if (book == null){
            throw new CustomException("Book payload is required", HttpStatus.BAD_REQUEST);
        }
        if (book.getIsbn() == null || book.getIsbn().isBlank()){
            throw new CustomException("ISBN is required", HttpStatus.BAD_REQUEST);
        }
        if (book.getTitle() == null || book.getTitle().isBlank()){
            throw new CustomException("Title is required", HttpStatus.BAD_REQUEST);
        }
        if (book.getPrice() == null || book.getPrice().signum() < 0){
            throw new CustomException("Price must be a non-negative value", HttpStatus.BAD_REQUEST);
        }
    }
}
