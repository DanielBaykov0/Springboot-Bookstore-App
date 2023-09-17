package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.entity.Country;
import baykov.daniel.springbootlibraryapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.CountryDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.CountryMapper;
import baykov.daniel.springbootlibraryapp.payload.response.CountryResponse;
import baykov.daniel.springbootlibraryapp.repository.CountryRepository;
import baykov.daniel.springbootlibraryapp.service.helper.CountryServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static baykov.daniel.springbootlibraryapp.constant.AppConstants.COUNTRY;
import static baykov.daniel.springbootlibraryapp.constant.AppConstants.ID;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final CountryServiceHelper countryServiceHelper;

    public CountryDTO createCountry(CountryDTO countryDTO) {
        Country country = CountryMapper.INSTANCE.dtoToEntity(countryDTO);
        countryRepository.save(country);
        return CountryMapper.INSTANCE.entityToDTO(country);
    }

    public CountryDTO getCountryById(Long countryId) {
        Country foundCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, countryId));
        return CountryMapper.INSTANCE.entityToDTO(foundCountry);
    }

    public CountryResponse getAllCountries(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Country> countries = countryRepository.findAll(pageable);
        return countryServiceHelper.getCountryResponse(countries);
    }

    public CountryDTO updateCountryById(Long countryId, CountryDTO countryDTO) {
        Country foundCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, countryId));
        foundCountry.update(CountryMapper.INSTANCE.dtoToEntity(countryDTO));

        countryRepository.save(foundCountry);
        return CountryMapper.INSTANCE.entityToDTO(foundCountry);
    }

    public void deleteCountryById(Long countryId) {
        Country foundCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, countryId));
        countryRepository.delete(foundCountry);
    }
}
