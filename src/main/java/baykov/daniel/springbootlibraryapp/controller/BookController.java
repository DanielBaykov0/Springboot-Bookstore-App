package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;
import baykov.daniel.springbootlibraryapp.service.BookService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import baykov.daniel.springbootlibraryapp.utils.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/books")
@Tag(name = "CRUD REST APIs for Book Resource")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(
            summary = "Create Book REST API",
            description = "Create Book REST API is used to save book into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(bookService.createBook(bookDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Book REST API",
            description = "Get Book REST API is used to get a particular book from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable(name = "id") Long paperBookId) {
        return ResponseEntity.ok(bookService.getBookById(paperBookId));
    }

    @Operation(
            summary = "Get All Books REST API",
            description = "Get All Books REST API is used to get all books from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping
    public ResponseEntity<BookResponse> getAllBooks(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(bookService.getAllBooks(pageNo, pageSize, sortBy, sortDir));
    }

    @Operation(
            summary = "Update Book REST API",
            description = "Update Book REST API is used to update a particular book in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PutMapping("{id}")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable(name = "id") Long paperBookId,
                                                  @Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBookByBookId(bookDTO, paperBookId));
    }

    @Operation(
            summary = "Delete Book REST API",
            description = "Delete Book REST API is used to delete a particular book from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable(name = "id") Long paperBookId) {
        bookService.deleteBookByBookId(paperBookId);
        return ResponseEntity.ok(Messages.BOOK_DELETE_MESSAGE);
    }

    @Operation(
            summary = "Get Books By Title, Genre, Description, Publication year or Author name REST API",
            description = "Search Books By Title, Genre, Description, Publication year or Author name REST API " +
                    "is used to search books by title, genre, description, publication year or author name in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("search")
    public ResponseEntity<BookResponse> getBooksByTitleOrByGenreOrByDescriptionOrByPublicationYearOrByAuthorName(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "year", required = false) Long publicationYear,
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
