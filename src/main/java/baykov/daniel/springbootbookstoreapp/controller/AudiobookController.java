package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.constant.AppConstants;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.AudiobookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CommentReviewRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.AudiobookResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CommentReviewResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.service.AudiobookService;
import baykov.daniel.springbootbookstoreapp.service.CommentReviewService;
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
import static baykov.daniel.springbootbookstoreapp.constant.Messages.AUDIOBOOK_DELETED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/audiobooks")
@Tag(name = "REST APIs for Audiobook Resource")
public class AudiobookController {

    private final AudiobookService audiobookService;
    private final CommentReviewService commentReviewService;

    @Operation(
            summary = "Create Audiobook REST API",
            description = "Create Audiobook REST API is used to save audiobook into the database"
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
    public ResponseEntity<AudiobookResponseDTO> createAudiobook(@Valid @RequestBody AudiobookRequestDTO audiobookRequestDTO) {
        log.info("Correlation ID: {}. Received request to create a new audiobook: {}", RequestData.getCorrelationId(), audiobookRequestDTO);

        AudiobookResponseDTO createdAudiobook = audiobookService.createAudiobook(audiobookRequestDTO);

        log.info("Correlation ID: {}. Audiobook created successfully: {}", RequestData.getCorrelationId(), createdAudiobook);
        return new ResponseEntity<>(createdAudiobook, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Audiobook REST API",
            description = "Get Audiobook REST API is used to get a particular audiobook from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{audiobookId}")
    public ResponseEntity<AudiobookResponseDTO> getAudiobookById(@PathVariable Long audiobookId) {
        log.info("Correlation ID: {}. Received request to retrieve audiobook with ID: {}", RequestData.getCorrelationId(), audiobookId);

        AudiobookResponseDTO audiobook = audiobookService.getAudiobookById(audiobookId);

        log.info("Correlation ID: {}. Retrieved audiobook successfully with ID {}: {}", RequestData.getCorrelationId(), audiobookId, audiobook);
        return ResponseEntity.ok(audiobook);
    }

    @Operation(
            summary = "Get All Audiobooks REST API",
            description = "Get All Audiobooks REST API is used to get all audiobooks from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping
    public ResponseEntity<GenericResponse<AudiobookResponseDTO>> getAllAudiobooks(
            @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to fetch audiobooks with parameters: pageNo={}, pageSize={}, sortBy={}, sortDir={}",
                RequestData.getCorrelationId(), pageNo, pageSize, sortBy, sortDir);

        GenericResponse<AudiobookResponseDTO> audiobooksResponse = audiobookService.getAllAudiobooks(pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Fetched audiobooks successfully. Total audiobooks retrieved: {}", RequestData.getCorrelationId(), audiobooksResponse.getContent().size());
        return ResponseEntity.ok(audiobooksResponse);
    }

    @Operation(
            summary = "Update Audiobook REST API",
            description = "Update Audiobook REST API is used to update an existing audiobook in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PutMapping("/{audiobookId}")
    public ResponseEntity<AudiobookResponseDTO> updateAudiobookById(
            @PathVariable Long audiobookId, @Valid @RequestBody AudiobookRequestDTO audiobookRequestDTO) {
        log.info("Correlation ID: {}. Received request to update audiobook with ID {}: {}", RequestData.getCorrelationId(), audiobookId, audiobookRequestDTO);

        AudiobookResponseDTO updatedAudiobook = audiobookService.updateAudiobookById(audiobookId, audiobookRequestDTO);

        log.info("Correlation ID: {}. Audiobook with ID {} updated successfully: {}", RequestData.getCorrelationId(), audiobookId, updatedAudiobook);
        return ResponseEntity.ok(updatedAudiobook);
    }

    @Operation(
            summary = "Delete Audiobook REST API",
            description = "Delete Audiobook REST API is used to delete a particular audiobook from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @DeleteMapping("/{audiobookId}")
    public ResponseEntity<String> deleteAudiobookById(@PathVariable Long audiobookId) {
        log.info("Correlation ID: {}. Received request to delete audiobook with ID: {}", RequestData.getCorrelationId(), audiobookId);

        audiobookService.deleteAudiobookById(audiobookId);

        log.info("Correlation ID: {}. Audiobook with ID {} deleted successfully", RequestData.getCorrelationId(), audiobookId);
        return ResponseEntity.ok(AUDIOBOOK_DELETED);
    }

    @Operation(
            summary = "Get Audiobooks By Searched Params REST API",
            description = "Get Searched Audiobooks REST API is used to fetch audiobooks based on search criteria"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/search")
    public ResponseEntity<GenericResponse<AudiobookResponseDTO>> getSearchedAudiobooks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "year", required = false) Integer publicationYear,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to get searched audiobooks. Title: {}, Author: {}, Description: {}, Category: {}, Publication Year: {}, PageNo: {}, PageSize: {}, SortBy: {}, SortDir: {}",
                RequestData.getCorrelationId(), title, author, description, category, publicationYear, pageNo, pageSize, sortBy, sortDir);

        GenericResponse<AudiobookResponseDTO> response = audiobookService.getSearchedAudiobooks(
                title, author, description, category, publicationYear, pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Returned {} audiobooks based on search criteria.", RequestData.getCorrelationId(), response.getContent().size());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Post Review REST API",
            description = "Post Review REST API is used to leave a review for the audiobook"
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
            summary = "Get All Reviews By Audiobook REST API",
            description = "Get All Reviews By Audiobook REST API is used to fetch all reviews for the audiobook"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{audiobookId}/reviews")
    public ResponseEntity<GenericResponse<CommentReviewResponseDTO>> getAllReviewsByAudiobook(
            @PathVariable Long audiobookId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postedAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        log.info("Correlation ID: {}. Retrieving all reviews by audiobook ID: {}.", RequestData.getCorrelationId(), audiobookId);

        GenericResponse<CommentReviewResponseDTO> response = commentReviewService.getProductReviewsById(audiobookId, pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Retrieved all reviews by audiobook ID: {}.", RequestData.getCorrelationId(), audiobookId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get First Rating For Audiobook By User REST API",
            description = "Get First Rating For Audiobook By User REST API is used to fetch first rating for audiobook by user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{audiobookId}/reviews/{userEmail}")
    @Hidden
    public ResponseEntity<Integer> getFirstRatingForAudiobookByUser(@PathVariable Long audiobookId, @PathVariable String userEmail) {
        log.info("Correlation ID: {}. Retrieving the first rating for audiobook with ID {} by user '{}'.", RequestData.getCorrelationId(), audiobookId, userEmail);

        Integer rating = commentReviewService.getFirstRatingForProductByUser(audiobookId, userEmail);

        log.debug("Correlation ID: {}. First rating found: {}", RequestData.getCorrelationId(), rating);
        return ResponseEntity.ok(rating);
    }
}
