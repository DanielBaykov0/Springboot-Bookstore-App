package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);

    BookDTO getBookById(long id);

    BookResponse getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir);

    BookDTO updateBookByBookId(BookDTO bookDTO, long id);

    void deleteBookByBookId(long id);

    void updateNumberOfBooksAfterBorrowing(Long id);

    void updateNumberOfBooksAfterReturning(Long id);
}
