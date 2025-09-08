package api.bookstore.catalog_service.utils;

import api.bookstore.catalog_service.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.Console;
import java.util.Set;
import java.util.stream.Collectors;

public class BookUtil {

    private static final Logger log = LoggerFactory.getLogger(BookUtil.class);

    public static Book BookDtoToDao(BookDTO book){
        if (book == null) return null;
        System.out.println("Received dto- "+ book);

        Book dao = new Book();
        dao.setId(book.getId());
        dao.setTitle(book.getTitle());
        dao.setIsbn(book.getIsbn());
        dao.setDescription(book.getDescription());
        dao.setPrice(book.getPrice());
        dao.setPublicationDate(book.getPublicationDate());

        // Publisher mapping (only id + name)
        if (book.getPublisher() != null) {
            dao.setPublisher(new Publisher(
                    book.getPublisher().getId(),
                    book.getPublisher().getName(),
                    book.getPublisher().getAddress(),
                    book.getPublisher().getBooks()
            ));
        }

        // Authors mapping (only id + name)
        if (book.getAuthors() != null) {
            dao.setAuthors(
                    book.getAuthors().stream()
                            .map(author -> new Author(author.getId(), author.getName(), author.getBio(), author.getBooks()))
                            .collect(Collectors.toSet())
            );
        }
        System.out.println("Converted dao- "+ dao);

        return dao;
    }

    public static BookDTO BookDaoToDto(Book book){
        if (book == null) return null;
        System.out.println("Received dao- "+ book);

        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        dto.setPublicationDate(book.getPublicationDate());

        // Publisher mapping (only id + name)
        if (book.getPublisher() != null) {
            dto.setPublisher(new PublisherDTO(
                    book.getPublisher().getId(),
                    book.getPublisher().getName(),
                    book.getPublisher().getAddress(),
                    book.getPublisher().getBooks()
            ));
        }

        // Authors mapping (only id + name)
        if (book.getAuthors() != null) {
            dto.setAuthors(
                    book.getAuthors().stream()
                            .map(author -> new AuthorDTO(author.getId(), author.getName(), author.getBio(), author.getBooks()))
                            .collect(Collectors.toSet())
            );
        }

        System.out.println("Converted dto- "+ dto);
        return dto;
    }
}
