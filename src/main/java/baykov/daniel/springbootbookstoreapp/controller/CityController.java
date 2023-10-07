package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.payload.dto.CityDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.service.CityService;
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
import static baykov.daniel.springbootbookstoreapp.constant.Messages.CITY_DELETED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cities")
@Tag(name = "CRUD REST APIs for City Resource")
public class CityController {

    private final CityService cityService;

    @Operation(
            summary = "Create City REST API",
            description = "Create City REST API is used to save city into the database"
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
    public ResponseEntity<CityDTO> createCity(@Valid @RequestBody CityDTO cityDTO) {
        log.info("Correlation ID: {}. Received request to create a new city: {}", RequestData.getCorrelationId(), cityDTO);

        CityDTO createdCity = cityService.createCity(cityDTO);

        log.info("Correlation ID: {}. City created successfully: {}", RequestData.getCorrelationId(), createdCity);
        return new ResponseEntity<>(createdCity, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get City REST API",
            description = "Get City REST API is used to get a particular city from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{cityId}")
    public ResponseEntity<CityDTO> getCityById(@PathVariable Long cityId) {
        log.info("Correlation ID: {}. Received request to retrieve city with ID: {}", RequestData.getCorrelationId(), cityId);

        CityDTO city = cityService.getCityById(cityId);

        log.info("Correlation ID: {}. Retrieved city successfully with ID {}: {}", RequestData.getCorrelationId(), cityId, city);
        return ResponseEntity.ok(city);
    }

    @Operation(
            summary = "Get All Cities REST API",
            description = "Get All Cities REST API is used to get all cities from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping
    public ResponseEntity<GenericResponse<CityDTO>> getAllCities(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to fetch cities with parameters: pageNo={}, pageSize={}, sortBy={}, sortDir={}",
                RequestData.getCorrelationId(), pageNo, pageSize, sortBy, sortDir);

        GenericResponse<CityDTO> citiesResponse = cityService.getAllCities(pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Fetched cities successfully. Total cities retrieved: {}", RequestData.getCorrelationId(), citiesResponse.getContent().size());
        return ResponseEntity.ok(citiesResponse);
    }

    @Operation(
            summary = "Update City REST API",
            description = "Update City REST API is used to update an existing city in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PutMapping("/{cityId}")
    public ResponseEntity<CityDTO> updateCityById(
            @PathVariable Long cityId, @Valid @RequestBody CityDTO cityDTO) {
        log.info("Correlation ID: {}. Received request to update city with ID {}: {}", RequestData.getCorrelationId(), cityId, cityDTO);

        CityDTO updatedCity = cityService.updateCityById(cityId, cityDTO);

        log.info("Correlation ID: {}. City with ID {} updated successfully: {}", RequestData.getCorrelationId(), cityId, updatedCity);
        return ResponseEntity.ok(updatedCity);
    }

    @Operation(
            summary = "Delete City REST API",
            description = "Delete City REST API is used to delete a particular city from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @DeleteMapping("/{cityId}")
    public ResponseEntity<String> deleteCityById(@PathVariable Long cityId) {
        log.info("Correlation ID: {}. Received request to delete city with ID: {}", RequestData.getCorrelationId(), cityId);

        cityService.deleteCityById(cityId);

        log.info("Correlation ID: {}. City with ID {} deleted successfully", RequestData.getCorrelationId(), cityId);
        return ResponseEntity.ok(CITY_DELETED);
    }

    @Operation(
            summary = "Get Cities By Searched Params REST API",
            description = "Get Cities By Searched Params REST API is used to fetch cities base on search criteria"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/search")
    public ResponseEntity<GenericResponse<CityDTO>> getAllCitiesByCountryName(
            @RequestParam String countryName,
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        log.info("Correlation ID: {}. Received request to get searched cities. Country Name: {}, PageNo: {}, PageSize: {}, SortBy: {}, SortDir: {}",
                RequestData.getCorrelationId(), countryName, pageNo, pageSize, sortBy, sortDir);

        GenericResponse<CityDTO> response = cityService.getAllCitiesByCountryName(countryName, pageNo, pageSize, sortBy, sortDir);

        log.info("Correlation ID: {}. Returned {} cities based on search criteria.", RequestData.getCorrelationId(), response.getContent().size());
        return ResponseEntity.ok(response);
    }
}
