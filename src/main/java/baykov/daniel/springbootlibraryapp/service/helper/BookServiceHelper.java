package baykov.daniel.springbootlibraryapp.service.helper;

import baykov.daniel.springbootlibraryapp.entity.Book;
import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.BookMapper;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookServiceHelper {

    public BookResponse getBookResponse(Page<Book> books) {
        List<BookDTO> content = BookMapper.INSTANCE.entityToDto(books.getContent());

        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(content);
        bookResponse.setPageNo(books.getNumber());
        bookResponse.setPageSize(books.getSize());
        bookResponse.setTotalElements(books.getTotalElements());
        bookResponse.setPageSize(books.getSize());
        bookResponse.setLast(books.isLast());
        return bookResponse;
    }
}
