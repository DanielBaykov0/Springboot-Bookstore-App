package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.constant.AppConstants;
import baykov.daniel.springbootlibraryapp.constant.Messages;
import baykov.daniel.springbootlibraryapp.payload.dto.AuthorDTO;
import baykov.daniel.springbootlibraryapp.payload.response.AuthorResponse;
import baykov.daniel.springbootlibraryapp.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "CRUD REST APIs for Author Resource")
public class AuthorController {

    private final AuthorService authorService;

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
    @PostMapping("/counties/{countryId}/cities/{cityId}/authors")
    public ResponseEntity<AuthorDTO> createAuthor(
            @Valid @RequestBody AuthorDTO authorDTO,
            @PathVariable Long countryId,
            @PathVariable Long cityId) {
        return new ResponseEntity<>(authorService.createAuthor(countryId, cityId, authorDTO), HttpStatus.CREATED);
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
    @GetMapping("/authors/{authorId}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    // swagger info
    @GetMapping("/countries/{countryId}/authors")
    public ResponseEntity<AuthorResponse> getAllAuthorsByCountry(
            @PathVariable Long countryId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        AuthorResponse authorResponse = authorService.getAllAuthorsByCountryId(countryId, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(authorResponse);
    }

    // swagger info
    @GetMapping("/cities/{cityId}/authors")
    public ResponseEntity<AuthorResponse> getAllAuthorsByCity(
            @PathVariable Long cityId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        AuthorResponse authorResponse = authorService.getAllAuthorsByCityId(cityId, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(authorResponse);
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
    @GetMapping("/authors")
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
    @PutMapping("/authors/{authorId}")
    public ResponseEntity<AuthorDTO> updateAuthorById(@PathVariable Long authorId,
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
    @DeleteMapping("/authors/{authorId}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.ok(Messages.AUTHOR_DELETED);
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
    @GetMapping("/authors")
    public ResponseEntity<AuthorResponse> getAllAuthorsByFirstNameOrLastNameOrCountry(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(authorService
                .getSearchedAuthors(name, country, city, pageNo, pageSize, sortBy, sortDir));
    }
}
