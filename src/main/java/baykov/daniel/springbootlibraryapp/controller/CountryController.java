package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.constant.AppConstants;
import baykov.daniel.springbootlibraryapp.payload.dto.CountryDTO;
import baykov.daniel.springbootlibraryapp.payload.response.CountryResponse;
import baykov.daniel.springbootlibraryapp.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static baykov.daniel.springbootlibraryapp.constant.Messages.COUNTRY_DELETED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    @PostMapping
    public ResponseEntity<CountryDTO> createCountry(@Valid @RequestBody CountryDTO countryDTO) {
        return new ResponseEntity<>(countryService.createCountry(countryDTO), HttpStatus.CREATED);
    }

    @GetMapping("{countryId}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Long countryId) {
        return ResponseEntity.ok(countryService.getCountryById(countryId));
    }

    @GetMapping
    public ResponseEntity<CountryResponse> getAllCountries(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(countryService.getAllCountries(pageNo, pageSize, sortBy, sortDir));
    }

    @PutMapping("{countryId}")
    public ResponseEntity<CountryDTO> updateCountryById(
            @PathVariable Long countryId, @Valid @RequestBody CountryDTO countryDTO) {
        return ResponseEntity.ok(countryService.updateCountryById(countryId, countryDTO));
    }

    @DeleteMapping("{countryId}")
    public ResponseEntity<String> deleteBookById(@PathVariable Long countryId) {
        countryService.deleteCountryById(countryId);
        return ResponseEntity.ok(COUNTRY_DELETED);
    }
}
