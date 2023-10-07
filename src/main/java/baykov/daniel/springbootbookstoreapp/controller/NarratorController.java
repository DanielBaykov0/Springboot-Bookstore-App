package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.NarratorRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.NarratorResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.service.NarratorService;
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
import static baykov.daniel.springbootbookstoreapp.constant.Messages.NARRATOR_DELETED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/narrators")
@Tag(name = "CRUD REST APIs for Narrator Resource")
public class NarratorController {

    private final NarratorService narratorService;

    @Operation(
            summary = "Create Narrator REST API",
            description = "Create Narrator REST API is used to save narrator into the database"
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
    public ResponseEntity<NarratorResponseDTO> createNarrator(@Valid @RequestBody NarratorRequestDTO narratorRequestDTO) {
        log.info("Correlation ID: {}. Received request to create a new narrator: {}", RequestData.getCorrelationId(), narratorRequestDTO);

        NarratorResponseDTO createdNarrator = narratorService.createNarrator(narratorRequestDTO);

        log.info("Correlation ID: {}. Narrator created successfully: {}", RequestData.getCorrelationId(), createdNarrator);
        return new ResponseEntity<>(createdNarrator, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Narrator REST API",
            description = "Get Narrator REST API is used to get a particular Narrator from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{narratorId}")
    public ResponseEntity<NarratorResponseDTO> getNarratorById(@PathVariable Long narratorId) {
        log.info("Correlation ID: {}. Received request to retrieve narrator with ID: {}", RequestData.getCorrelationId(), narratorId);

        NarratorResponseDTO narrator = narratorService.getNarratorById(narratorId);

        log.info("Correlation ID: {}. Retrieved narrator successfully with ID {}: {}", RequestData.getCorrelationId(), narratorId, narrator);
        return ResponseEntity.ok(narrator);
    }

    @Operation(
            summary = "Get All Narrators REST API",
            description = "Get All Narrators REST API is used to get all narrators from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping
    public ResponseEntity<GenericResponse<NarratorResponseDTO>> getAllNarrators(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to fetch narrators with parameters: pageNo={}, pageSize={}, sortBy={}, sortDir={}",
                RequestData.getCorrelationId(), pageNo, pageSize, sortBy, sortDir);

        GenericResponse<NarratorResponseDTO> narratorResponse = narratorService.getAllNarrators(pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Fetched narrators successfully. Total narrators retrieved: {}", RequestData.getCorrelationId(), narratorResponse.getContent().size());
        return ResponseEntity.ok(narratorResponse);
    }

    @Operation(
            summary = "Update Narrator REST API",
            description = "Update Narrator REST API is used to update an existing narrator in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PutMapping("/{narratorId}")
    public ResponseEntity<NarratorResponseDTO> updateNarratorById(@PathVariable Long narratorId, @Valid @RequestBody NarratorRequestDTO narratorRequestDTO) {
        log.info("Correlation ID: {}. Received request to update narrator with ID {}: {}", RequestData.getCorrelationId(), narratorId, narratorRequestDTO);

        NarratorResponseDTO updatedNarrator = narratorService.updateNarratorById(narratorId, narratorRequestDTO);

        log.info("Correlation ID: {}. Narrator with ID {} updated successfully: {}", RequestData.getCorrelationId(), narratorId, narratorRequestDTO);
        return ResponseEntity.ok(updatedNarrator);
    }

    @Operation(
            summary = "Delete Narrator REST API",
            description = "Delete Narrator REST API is used to delete a particular narrator from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @DeleteMapping("/{narratorId}")
    public ResponseEntity<String> deleteNarratorById(@PathVariable Long narratorId) {
        log.info("Correlation ID: {}. Received request to delete narrator with ID: {}", RequestData.getCorrelationId(), narratorId);

        narratorService.deleteNarratorById(narratorId);

        log.info("Correlation ID: {}. Narrator with ID {} deleted successfully", RequestData.getCorrelationId(), narratorId);
        return ResponseEntity.ok(NARRATOR_DELETED);
    }

    @Operation(
            summary = "Get Narrators By Searched Params REST API",
            description = "Get Searched Narrators REST API is used to fetch narrators based on search criteria"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/search")
    public ResponseEntity<GenericResponse<NarratorResponseDTO>> getAllNarratorsBySearchedParams(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "country", required = false) Long countryId,
            @RequestParam(value = "city", required = false) Long cityId,
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to get searched narrators. Name: {}, Country ID: {}, City ID: {}, PageNo: {}, PageSize: {}, SortBy: {}, SortDir: {}",
                RequestData.getCorrelationId(), name, countryId, cityId, pageNo, pageSize, sortBy, sortDir);

        GenericResponse<NarratorResponseDTO> response = narratorService
                .getSearchedNarrators(name, countryId, cityId, pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Returned {} narrators based on search criteria.", RequestData.getCorrelationId(), response.getContent().size());
        return ResponseEntity.ok(response);
    }
}
