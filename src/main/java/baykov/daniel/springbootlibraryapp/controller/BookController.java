package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.constant.AppConstants;
import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;
import baykov.daniel.springbootlibraryapp.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static baykov.daniel.springbootlibraryapp.constant.AppConstants.*;
import static baykov.daniel.springbootlibraryapp.constant.Messages.BOOK_DELETED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/books")
@Tag(name = "CRUD REST APIs for Book Resource")
public class BookController {

    private final BookService bookService;

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
    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
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
            @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
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
    @PutMapping("/{bookId}")
    public ResponseEntity<BookDTO> updateBookById(
            @PathVariable Long bookId, @Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBookByBookId(bookId, bookDTO));
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
    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBookById(@PathVariable Long bookId) {
        bookService.deleteBookByBookId(bookId);
        return ResponseEntity.ok(BOOK_DELETED);
    }

    @Operation(
            summary = "Get Books By Title REST API",
            description = "Search Books By Title REST API is used to search books by title in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping
    public ResponseEntity<BookResponse> getBooksByTitle(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(bookService.getAllBooksByTitle(title, pageNo, pageSize, sortBy, sortDir));
    }

    // swagger info
    @GetMapping
    public ResponseEntity<BookResponse> getBooksByAuthorName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(bookService.getAllBooksByAuthorName(name, pageNo, pageSize, sortBy, sortDir));
    }

    // swagger info
    @GetMapping
    public ResponseEntity<BookResponse> getBooksByDescription(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(bookService.getAllBooksByDescription(description, pageNo, pageSize, sortBy, sortDir));
    }

    // swagger info
    @GetMapping
    public ResponseEntity<BookResponse> getBooksByGenre(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(bookService.getAllBooksByGenre(genre, pageNo, pageSize, sortBy, sortDir));
    }

    // swagger info
    @GetMapping
    public ResponseEntity<BookResponse> getBooksByPublicationYear(
            @RequestParam(value = "year", required = false) int publicationYear,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(bookService.getAllBooksByPublicationYear(publicationYear, pageNo, pageSize, sortBy, sortDir));
    }
}
