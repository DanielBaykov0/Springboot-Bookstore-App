package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.payload.dto.CityDTO;
import baykov.daniel.springbootlibraryapp.payload.response.CityResponse;
import baykov.daniel.springbootlibraryapp.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static baykov.daniel.springbootlibraryapp.constant.AppConstants.*;
import static baykov.daniel.springbootlibraryapp.constant.Messages.CITY_DELETED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CityController {

    private final CityService cityService;

    @PostMapping("/countries/{countryId}/cities")
    public ResponseEntity<CityDTO> createCity(
            @PathVariable Long countryId, @Valid @RequestBody CityDTO cityDTO) {
        ResponseEntity<CityDTO> response = new ResponseEntity<>(cityService.createCity(countryId, cityDTO), HttpStatus.CREATED);
        return response;
    }

    @GetMapping("/cities/{cityId}")
    public ResponseEntity<CityDTO> getCityById(@PathVariable Long cityId) {
        CityDTO cityDTO = cityService.getCityById(cityId);
        return ResponseEntity.ok(cityDTO);
    }

    @GetMapping("/cities")
    public ResponseEntity<CityResponse> getAllCities(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        CityResponse cityResponse = cityService.getAllCities(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(cityResponse);
    }

    @GetMapping("/countries/{countryId}/cities")
    public ResponseEntity<CityResponse> getAllCitiesByCountryId(
            @PathVariable Long countryId,
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIR, required = false) String sortDir) {
        CityResponse cityResponse = cityService.getAllCitiesByCountryId(countryId, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(cityResponse);
    }

    @PutMapping("/cities/{cityId}")
    public ResponseEntity<CityDTO> updateCityById(
            @PathVariable Long cityId, @Valid @RequestBody CityDTO cityDTO) {
        CityDTO updatedCity = cityService.updateCityById(cityId, cityDTO);
        return ResponseEntity.ok(updatedCity);
    }

    @DeleteMapping("/cities/{cityId}")
    public ResponseEntity<String> deleteCityById(@PathVariable Long cityId) {
        cityService.deleteCityById(cityId);
        return ResponseEntity.ok(CITY_DELETED);
    }
}
