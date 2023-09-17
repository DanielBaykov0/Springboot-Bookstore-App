package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.entity.City;
import baykov.daniel.springbootlibraryapp.entity.Country;
import baykov.daniel.springbootlibraryapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.CityDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.CityMapper;
import baykov.daniel.springbootlibraryapp.payload.response.CityResponse;
import baykov.daniel.springbootlibraryapp.repository.CityRepository;
import baykov.daniel.springbootlibraryapp.repository.CountryRepository;
import baykov.daniel.springbootlibraryapp.service.helper.CityServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static baykov.daniel.springbootlibraryapp.constant.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final CityServiceHelper cityServiceHelper;

    public CityDTO createCity(Long countryId, CityDTO cityDTO) {
        City city = CityMapper.INSTANCE.dtoToEntity(cityDTO);
        Country foundCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, countryId));

        city.setCountry(foundCountry);
        cityRepository.save(city);
        return CityMapper.INSTANCE.entityToDTO(city);
    }

    public CityDTO getCityById(Long cityId) {
        City foundCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, cityId));
        return CityMapper.INSTANCE.entityToDTO(foundCity);
    }

    public CityResponse getAllCities(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<City> cities = cityRepository.findAll(pageable);
        return cityServiceHelper.getCityResponse(cities);
    }

    public CityResponse getAllCitiesByCountryId(Long countryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<City> cities = cityRepository.findAllByCountryId(countryId, pageable);
        return cityServiceHelper.getCityResponse(cities);
    }

    public CityDTO updateCityById(Long cityId, CityDTO cityDTO) {
        City foundCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, cityId));
        foundCity.setName(cityDTO.getName());
        cityRepository.save(foundCity);
        return CityMapper.INSTANCE.entityToDTO(foundCity);
    }

    public void deleteCityById(Long cityId) {
        City foundCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, cityId));
        cityRepository.delete(foundCity);
    }
}
