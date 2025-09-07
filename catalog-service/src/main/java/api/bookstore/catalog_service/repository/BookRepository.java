package api.bookstore.catalog_service.repository;

import api.bookstore.catalog_service.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByTitleContainingIgnoreCase(String title);
}
