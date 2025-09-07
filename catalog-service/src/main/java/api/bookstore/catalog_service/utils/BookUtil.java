package api.bookstore.catalog_service.utils;

import api.bookstore.catalog_service.models.Book;
import api.bookstore.catalog_service.models.BookDTO;
import org.springframework.beans.BeanUtils;

public class BookUtil {

    public static Book BookDtoToDao(BookDTO dto){
        Book dao = new Book();
        BeanUtils.copyProperties(dto, dao);
        return dao;
    }

    public static BookDTO BookDtoToDao(Book dao){
        BookDTO dto = new BookDTO();
        BeanUtils.copyProperties(dao, dto);
        return dto;
    }
}
