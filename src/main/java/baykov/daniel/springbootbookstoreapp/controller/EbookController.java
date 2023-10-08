package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.constant.AppConstants;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CommentReviewRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.EbookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CommentReviewResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.EbookResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.service.CommentReviewService;
import baykov.daniel.springbootbookstoreapp.service.EbookService;
import io.swagger.v3.oas.annotations.Hidden;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.EBOOK_DELETED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/ebooks")
@Tag(name = "REST APIs for Ebook Resource")
public class EbookController {

    private final EbookService ebookService;
    private final CommentReviewService commentReviewService;

    @Operation(
            summary = "Create Ebook REST API",
            description = "Create Ebook REST API is used to save ebook into database"
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
    public ResponseEntity<EbookResponseDTO> createEbook(@Valid @RequestBody EbookRequestDTO ebookRequestDTO) {
        log.info("Correlation ID: {}. Received request to create a new ebook: {}", RequestData.getCorrelationId(), ebookRequestDTO);

        EbookResponseDTO createdEbook = ebookService.createEbook(ebookRequestDTO);

        log.info("Correlation ID: {}. Ebook created successfully: {}", RequestData.getCorrelationId(), createdEbook);

        return new ResponseEntity<>(createdEbook, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Ebook REST API",
            description = "Get Ebook REST API is used to get a particular ebook from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{ebookId}")
    public ResponseEntity<EbookResponseDTO> getEbookById(@PathVariable Long ebookId) {
        log.info("Correlation ID: {}. Received request to retrieve ebook with ID: {}", RequestData.getCorrelationId(), ebookId);

        EbookResponseDTO ebook = ebookService.getEbookById(ebookId);

        log.info("Correlation ID: {}. Retrieved ebook successfully with ID {}: {}", RequestData.getCorrelationId(), ebookId, ebook);
        return ResponseEntity.ok(ebook);
    }

    @Operation(
            summary = "Get All Ebooks REST API",
            description = "Get All Ebooks REST API is used to get all ebooks from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping
    public ResponseEntity<GenericResponse<EbookResponseDTO>> getAllEbooks(
            @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to fetch ebooks with parameters: pageNo={}, pageSize={}, sortBy={}, sortDir={}",
                RequestData.getCorrelationId(), pageNo, pageSize, sortBy, sortDir);

        GenericResponse<EbookResponseDTO> ebooksResponse = ebookService.getAllEbooks(pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Fetched ebooks successfully. Total ebooks retrieved: {}", RequestData.getCorrelationId(), ebooksResponse.getContent().size());
        return ResponseEntity.ok(ebooksResponse);
    }

    @Operation(
            summary = "Update Ebook REST API",
            description = "Update Ebook REST API is used to update an existing ebook in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PutMapping("/{ebookId}")
    public ResponseEntity<EbookResponseDTO> updateEbookById(
            @PathVariable Long ebookId, @Valid @RequestBody EbookRequestDTO ebookRequestDTO) {
        log.info("Correlation ID: {}. Received request to update ebook with ID {}: {}", RequestData.getCorrelationId(), ebookId, ebookRequestDTO);

        EbookResponseDTO updatedEbook = ebookService.updateEBookById(ebookId, ebookRequestDTO);

        log.info("Correlation ID: {}. Ebook with ID {} updated successfully: {}", RequestData.getCorrelationId(), ebookId, updatedEbook);
        return ResponseEntity.ok(updatedEbook);
    }

    @Operation(
            summary = "Delete Ebook REST API",
            description = "Delete Ebook REST API is used to delete a particular ebook from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @DeleteMapping("/{ebookId}")
    public ResponseEntity<String> deleteEbookById(@PathVariable Long ebookId) {
        log.info("Correlation ID: {}. Received request to delete ebook with ID: {}", RequestData.getCorrelationId(), ebookId);

        ebookService.deleteEbookById(ebookId);

        log.info("Correlation ID: {}. Ebook with ID {} deleted successfully", RequestData.getCorrelationId(), ebookId);
        return ResponseEntity.ok(EBOOK_DELETED);
    }

    @Operation(
            summary = "Get Ebooks By Searched Params REST API",
            description = "Get Searched Ebooks REST API is used to fetch ebooks based on search criteria"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/search")
    public ResponseEntity<GenericResponse<EbookResponseDTO>> getSearchedEbooks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "year", required = false) Integer publicationYear,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to get searched ebooks. Title: {}, Author: {}, Description: {}, Category: {}, Publication Year: {}, PageNo: {}, PageSize: {}, SortBy: {}, SortDir: {}",
                RequestData.getCorrelationId(), title, author, description, category, publicationYear, pageNo, pageSize, sortBy, sortDir);

        GenericResponse<EbookResponseDTO> response = ebookService.getSearchedEbooks(
                title, author, description, category, publicationYear, pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Returned {} ebooks based on search criteria.", RequestData.getCorrelationId(), response.getContent().size());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Post Review REST API",
            description = "Post Review REST API is used to leave a review for the ebook"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/reviews")
    public ResponseEntity<CommentReviewResponseDTO> postReview(
            @RequestBody @Valid CommentReviewRequestDTO requestDto,
            Authentication authentication) {
        log.info("Correlation ID: {}. Creating a new review.", RequestData.getCorrelationId());
        String username = authentication.getName();
        log.debug("Correlation ID: {}. User '{}' is creating a review.", RequestData.getCorrelationId(), username);

        ResponseEntity<CommentReviewResponseDTO> responseEntity =
                new ResponseEntity<>(commentReviewService.postReview(requestDto, authentication), HttpStatus.CREATED);

        log.info("Correlation ID: {}. New review created.", RequestData.getCorrelationId());
        return responseEntity;
    }

    @Operation(
            summary = "Get All Reviews By Ebook REST API",
            description = "Get All Reviews By Ebook REST API is used to fetch all reviews for the ebook"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{ebookId}/reviews")
    public ResponseEntity<GenericResponse<CommentReviewResponseDTO>> getAllReviewsByEbook(
            @PathVariable Long ebookId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postedAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        log.info("Correlation ID: {}. Retrieving all reviews by ebook ID: {}.", RequestData.getCorrelationId(), ebookId);

        GenericResponse<CommentReviewResponseDTO> response = commentReviewService.getProductReviewsById(ebookId, pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Retrieved all reviews by ebook ID: {}.", RequestData.getCorrelationId(), ebookId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get First Rating For Ebook By User REST API",
            description = "Get First Rating For Ebook By User REST API is used to fetch first rating for ebook by user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{ebookId}/reviews/{userEmail}")
    @Hidden
    public ResponseEntity<Integer> getFirstRatingForEbookByUser(@PathVariable Long ebookId, @PathVariable String userEmail) {
        log.info("Correlation ID: {}. Retrieving the first rating for ebook with ID {} by user '{}'.", RequestData.getCorrelationId(), ebookId, userEmail);

        Integer rating = commentReviewService.getFirstRatingForProductByUser(ebookId, userEmail);

        log.debug("Correlation ID: {}. First rating found: {}", RequestData.getCorrelationId(), rating);
        return ResponseEntity.ok(rating);
    }
}
