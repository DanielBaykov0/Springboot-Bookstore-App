package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Country;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.CountryDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.CountryMapper;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.CountryRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.COUNTRY;
import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final ServiceUtil serviceUtil;

    @Transactional
    public CountryDTO createCountry(CountryDTO countryDTO) {
        log.info("Creating country...");
        Country country = CountryMapper.INSTANCE.dtoToEntity(countryDTO);
        countryRepository.save(country);
        log.info("Created country with ID: {}", country.getId());
        return CountryMapper.INSTANCE.entityToDTO(country);
    }

    public CountryDTO getCountryById(Long countryId) {
        log.info("Getting country by ID: {}", countryId);
        Country foundCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, countryId));
        log.info("Country with ID {} retrieved successfully.", countryId);
        return CountryMapper.INSTANCE.entityToDTO(foundCountry);
    }

    public GenericResponse<CountryDTO> getAllCountries(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving countries...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Country> countries = countryRepository.findAll(pageable);
        List<CountryDTO> content = CountryMapper.INSTANCE.entityToDTO(countries.getContent());
        log.info("Retrieved {} countries.", countries.getContent().size());
        return serviceUtil.createGenericResponse(countries, content);
    }

    @Transactional
    public CountryDTO updateCountryById(Long countryId, CountryDTO countryDTO) {
        log.info("Start updating country with ID: {}", countryId);
        Country foundCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, countryId));
        foundCountry.update(CountryMapper.INSTANCE.dtoToEntity(countryDTO));

        countryRepository.save(foundCountry);
        log.info("Country with ID {} updated successfully.", countryId);
        return CountryMapper.INSTANCE.entityToDTO(foundCountry);
    }

    @Transactional
    public void deleteCountryById(Long countryId) {
        log.info("Deleting country with ID: {}", countryId);
        Country foundCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, countryId));
        countryRepository.delete(foundCountry);
        log.info("Country with ID {} deleted successfully.", countryId);
    }
}
