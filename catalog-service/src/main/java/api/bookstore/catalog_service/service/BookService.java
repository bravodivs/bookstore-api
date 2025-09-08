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

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final BookEventPublisher eventPublisher;

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public BookService(BookRepository bookRepository, BookEventPublisher eventPublisher){
        this.bookRepository = bookRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<BookDTO> getAllBooks(){
        var books = bookRepository.findAll();
        if (!books.isEmpty()){
            List<BookDTO> booksDto = new ArrayList<>(books.size());
            books.forEach(b -> {
                booksDto.add(BookUtil.BookDaoToDto(b));
            });
        }
        logger.warn("No books found in database");
        throw new CustomException("No books found in database", HttpStatus.NOT_FOUND);
    }

    public UUID createBook(BookDTO book) {
        Book saved = bookRepository.save(BookUtil.BookDtoToDao(book));
        eventPublisher.publishBookCreated(saved);
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
    public List<Book> search(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    // todo add logic to delete by other params such as title
    public boolean deleteBook(UUID id) {
        if (this.getBookById(id) != null){
            bookRepository.deleteById(id);
    return true;
        }
        return false;
    }
}
