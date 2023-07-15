package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);

    BookDTO getBookById(Long id);

    BookResponse getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir);

    BookDTO updateBookByBookId(BookDTO bookDTO, Long id);

    void deleteBookByBookId(Long id);

    void updateNumberOfBooksAfterBorrowing(Long id);

    void updateNumberOfBooksAfterReturning(Long id);

    BookResponse getBooksByTitleOrByGenreOrByDescriptionOrByPublicationYearOrByAuthorName(
            String title, String genre, String description, Long publicationYear, String authorFirstName, String authorLastName, int pageNo, int pageSize, String sortBy, String sortDir);
}
