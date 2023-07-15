package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;

public interface PaperBookService {

    BookDTO createPaperBook(BookDTO bookDTO);

    BookDTO getPaperBookById(long id);

    BookResponse getAllPaperBooks(int pageNo, int pageSize, String sortBy, String sortDir);

    BookDTO updatePaperBook(BookDTO bookDTO, long id);

    void deletePaperBook(long id);

    void updateNumberOfBooksAfterBorrowing(Long id);

    void updateNumberOfBooksAfterReturning(Long id);
}
