package baykov.daniel.springbootlibraryapp.controllers;

import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;
import baykov.daniel.springbootlibraryapp.services.BookService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import baykov.daniel.springbootlibraryapp.utils.Messages;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(bookService.createBook(bookDTO), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable(name = "id") Long paperBookId) {
        return ResponseEntity.ok(bookService.getBookById(paperBookId));
    }

    @GetMapping
    public ResponseEntity<BookResponse> getAllBooks(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(bookService.getAllBooks(pageNo, pageSize, sortBy, sortDir));
    }

    @PutMapping("{id}")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable(name = "id") Long paperBookId,
                                                  @Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBookByBookId(bookDTO, paperBookId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable(name = "id") Long paperBookId) {
        bookService.deleteBookByBookId(paperBookId);
        return ResponseEntity.ok(Messages.BOOK_DELETE_MESSAGE);
    }

    @GetMapping("search")
    public ResponseEntity<BookResponse> getBooksByTitleOrByGenreOrByDescriptionOrByPublicationYearOrByAuthorName(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "year", required = false) int publicationYear,
            @RequestParam(value = "firstName", required = false) String authorFirstName,
            @RequestParam(value = "lastName", required = false) String authorLastName,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(bookService.getBooksByTitleOrByGenreOrByDescriptionOrByPublicationYearOrByAuthorName(
                title, genre, description, publicationYear, authorFirstName, authorLastName, pageNo, pageSize, sortBy, sortDir));
    }
}
