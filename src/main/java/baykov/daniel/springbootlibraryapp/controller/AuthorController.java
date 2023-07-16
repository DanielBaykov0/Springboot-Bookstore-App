package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.payload.dto.AuthorDTO;
import baykov.daniel.springbootlibraryapp.payload.response.AuthorResponse;
import baykov.daniel.springbootlibraryapp.service.AuthorService;
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
@RequestMapping("api/v1/authors")
@Tag(name = "CRUD REST APIs for Author Resource")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(
            summary = "Create Author REST API",
            description = "Create Author REST API is used to save author into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        return new ResponseEntity<>(authorService.createAuthor(authorDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Author By Id REST API",
            description = "Get Author By Id REST API is used to get a single author from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable(name = "id") Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    @Operation(
            summary = "Get All Authors REST API",
            description = "Get All Authors REST API is used to fetch all the authors from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping
    public ResponseEntity<AuthorResponse> getAllAuthors(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(authorService.getAllAuthors(pageNo, pageSize, sortBy, sortDir));
    }

    @Operation(
            summary = "Update Author REST API",
            description = "Update Author REST API is used to update a particular author in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PutMapping("{id}")
    public ResponseEntity<AuthorDTO> updateAuthorById(@PathVariable(name = "id") Long authorId,
                                                      @Valid @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.updateAuthorById(authorDTO, authorId));
    }

    @Operation(
            summary = "Delete Author REST API",
            description = "Delete Author REST API is used to delete author from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable(name = "id") Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.ok(Messages.AUTHOR_DELETE_MESSAGE);
    }

    @Operation(
            summary = "Get Author By First Name, Last Name Or Country REST API",
            description = "Search Author By First Name, Last Name Or Country REST API " +
                    "is used to search authors by first name, last name or country from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("search")
    public ResponseEntity<AuthorResponse> getAllAuthorsByAuthorFirstNameOrAuthorLastNameOrAuthorCountry(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(authorService
                .getAllAuthorsByAuthorFirstNameOrAuthorLastNameOrAuthorCountry(firstName, lastName, country, pageNo, pageSize, sortBy, sortDir));
    }
}
