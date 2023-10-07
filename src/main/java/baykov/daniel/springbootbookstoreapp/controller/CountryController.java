package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.payload.dto.CountryDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.service.CountryService;
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
import static baykov.daniel.springbootbookstoreapp.constant.Messages.COUNTRY_DELETED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/countries")
@Tag(name = "CRUD REST APIs for Country Resource")
public class CountryController {

    private final CountryService countryService;

    @Operation(
            summary = "Create Country REST API",
            description = "Create Country REST API is used to save country into the database"
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
    public ResponseEntity<CountryDTO> createCountry(@Valid @RequestBody CountryDTO countryDTO) {
        log.info("Correlation ID: {}. Received request to create a new country: {}", RequestData.getCorrelationId(), countryDTO);

        CountryDTO createdCountry = countryService.createCountry(countryDTO);

        log.info("Correlation ID: {}. Country created successfully: {}", RequestData.getCorrelationId(), createdCountry);
        return new ResponseEntity<>(createdCountry, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Country REST API",
            description = "Get Country REST API is used to get a particular country from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{countryId}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Long countryId) {
        log.info("Correlation ID: {}. Received request to retrieve country with ID: {}", RequestData.getCorrelationId(), countryId);

        CountryDTO country = countryService.getCountryById(countryId);

        log.info("Correlation ID: {}. Retrieved country successfully with ID {}: {}", RequestData.getCorrelationId(), countryId, country);
        return ResponseEntity.ok(country);
    }

    @Operation(
            summary = "Get All Countries REST API",
            description = "Get All Countries REST API is used to get all countries from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping
    public ResponseEntity<GenericResponse<CountryDTO>> getAllCountries(
            @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to fetch countries with parameters: pageNo={}, pageSize={}, sortBy={}, sortDir={}",
                RequestData.getCorrelationId(), pageNo, pageSize, sortBy, sortDir);

        GenericResponse<CountryDTO> countriesResponse = countryService.getAllCountries(pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Fetched countries successfully. Total countries retrieved: {}", RequestData.getCorrelationId(), countriesResponse.getContent().size());
        return ResponseEntity.ok(countriesResponse);
    }

    @Operation(
            summary = "Update Country REST API",
            description = "Update Country REST API is used to update an existing country in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PutMapping("/{countryId}")
    public ResponseEntity<CountryDTO> updateCountryById(
            @PathVariable Long countryId, @Valid @RequestBody CountryDTO countryDTO) {
        log.info("Correlation ID: {}. Received request to update country with ID {}: {}", RequestData.getCorrelationId(), countryId, countryDTO);

        CountryDTO updatedCountry = countryService.updateCountryById(countryId, countryDTO);

        log.info("Correlation ID: {}. Country with ID {} updated successfully: {}", RequestData.getCorrelationId(), countryId, updatedCountry);
        return ResponseEntity.ok(updatedCountry);
    }

    @Operation(
            summary = "Delete Country REST API",
            description = "Delete Country REST API is used to delete a particular country from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @DeleteMapping("/{countryId}")
    public ResponseEntity<String> deleteCountryById(@PathVariable Long countryId) {
        log.info("Correlation ID: {}. Received request to delete country with ID: {}", RequestData.getCorrelationId(), countryId);

        countryService.deleteCountryById(countryId);

        log.info("Correlation ID: {}. Country with ID {} deleted successfully", RequestData.getCorrelationId(), countryId);
        return ResponseEntity.ok(COUNTRY_DELETED);
    }
}
