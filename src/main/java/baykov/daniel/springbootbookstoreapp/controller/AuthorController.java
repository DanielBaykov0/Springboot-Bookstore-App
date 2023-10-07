package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.AuthorRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.AuthorResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.AUTHOR_DELETED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/authors")
@Tag(name = "CRUD REST APIs for Author Resource")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(
            summary = "Create Author REST API",
            description = "Create Author REST API is used to save author into the database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@Valid @RequestBody AuthorRequestDTO authorRequestDTO) {
        log.info("Correlation ID: {}. Received request to create a new author: {}", RequestData.getCorrelationId(), authorRequestDTO);

        AuthorResponseDTO createdAuthor = authorService.createAuthor(authorRequestDTO);

        log.info("Correlation ID: {}. Author created successfully: {}", RequestData.getCorrelationId(), createdAuthor);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Author REST API",
            description = "Get Author REST API is used to get a particular author from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long authorId) {
        log.info("Correlation ID: {}. Received request to retrieve author with ID: {}", RequestData.getCorrelationId(), authorId);

        AuthorResponseDTO author = authorService.getAuthorById(authorId);

        log.info("Correlation ID: {}. Retrieved author successfully with ID {}: {}", RequestData.getCorrelationId(), authorId, author);
        return ResponseEntity.ok(author);
    }

    @Operation(
            summary = "Get All Authors REST API",
            description = "Get All Authors REST API is used to get all authors from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping
    public ResponseEntity<GenericResponse<AuthorResponseDTO>> getAllAuthors(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to fetch authors with parameters: pageNo={}, pageSize={}, sortBy={}, sortDir={}",
                RequestData.getCorrelationId(), pageNo, pageSize, sortBy, sortDir);

        GenericResponse<AuthorResponseDTO> authorsResponse = authorService.getAllAuthors(pageNo, pageSize, sortBy, sortDir);


        log.info("Correlation ID: {}. Fetched authors successfully. Total authors retrieved: {}", RequestData.getCorrelationId(), authorsResponse.getContent().size());
        return ResponseEntity.ok(authorsResponse);
    }

    @Operation(
            summary = "Update Author REST API",
            description = "Update Author REST API is used to update an existing author in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDTO> updateAuthorById(
            @PathVariable Long authorId, @Valid @RequestBody AuthorRequestDTO authorRequestDTO) {
        log.info("Correlation ID: {}. Received request to update author with ID {}: {}", RequestData.getCorrelationId(), authorId, authorRequestDTO);

        AuthorResponseDTO updatedAuthor = authorService.updateAuthorById(authorRequestDTO, authorId);

        log.info("Correlation ID: {}. Author with ID {} updated successfully: {}", RequestData.getCorrelationId(), authorId, updatedAuthor);
        return ResponseEntity.ok(updatedAuthor);
    }

    @Operation(
            summary = "Delete Author REST API",
            description = "Delete Author REST API is used to delete a particular author from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @DeleteMapping("/{authorId}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable Long authorId) {
        log.info("Correlation ID: {}. Received request to delete author with ID: {}", RequestData.getCorrelationId(), authorId);

        authorService.deleteAuthorById(authorId);

        log.info("Correlation ID: {}. Author with ID {} deleted successfully", RequestData.getCorrelationId(), authorId);
        return ResponseEntity.ok(AUTHOR_DELETED);
    }

    @Operation(
            summary = "Get Author By Searched Params REST API",
            description = "Get Searched Author REST API is used to fetch authors based on search criteria"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/search")
    public ResponseEntity<GenericResponse<AuthorResponseDTO>> getAllAuthorsByFirstNameOrLastNameOrCountryOrCity(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "country", required = false) Long countryId,
            @RequestParam(value = "city", required = false) Long cityId,
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to get searched authors. Name: {}, Country ID: {}, City ID: {}, PageNo: {}, PageSize: {}, SortBy: {}, SortDir: {}",
                RequestData.getCorrelationId(), name, countryId, cityId, pageNo, pageSize, sortBy, sortDir);

        GenericResponse<AuthorResponseDTO> response = authorService
                .getSearchedAuthors(name, countryId, cityId, pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Returned {} authors based on search criteria.", RequestData.getCorrelationId(), response.getContent().size());
        return ResponseEntity.ok(response);
    }
}
