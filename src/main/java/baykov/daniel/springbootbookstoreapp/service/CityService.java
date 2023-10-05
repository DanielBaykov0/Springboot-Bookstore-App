package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.City;
import baykov.daniel.springbootbookstoreapp.entity.Country;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.CityDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.CityMapper;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.CityRepository;
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

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final ServiceUtil serviceUtil;

    @Transactional
    public CityDTO createCity(CityDTO cityDTO) {
        log.info("Creating city...");
        City city = CityMapper.INSTANCE.dtoToEntity(cityDTO);
        Country foundCountry = countryRepository.findById(cityDTO.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, cityDTO.getCountryId()));

        city.setCountry(foundCountry);
        cityRepository.save(city);
        log.info("Created city with ID: {}", city.getId());
        return CityMapper.INSTANCE.entityToDTO(city);
    }

    public CityDTO getCityById(Long cityId) {
        log.info("Getting city by ID: {}", cityId);
        City foundCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, cityId));
        log.info("City with ID {} retrieved successfully.", cityId);
        return CityMapper.INSTANCE.entityToDTO(foundCity);
    }

    public GenericResponse<CityDTO> getAllCities(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving cities...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<City> cities = cityRepository.findAll(pageable);
        List<CityDTO> content = CityMapper.INSTANCE.entityToDTO(cities.getContent());
        log.info("Retrieved {} cities.", cities.getContent().size());
        return serviceUtil.createGenericResponse(cities, content);
    }

    public GenericResponse<CityDTO> getAllCitiesByCountryName(String countryName, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving cities by country NAME: {}...", countryName);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<City> cities = cityRepository.findAllByCountryNameIgnoreCase(countryName, pageable);
        List<CityDTO> content = CityMapper.INSTANCE.entityToDTO(cities.getContent());
        log.info("Retrieved {} cities.", cities.getContent().size());
        return serviceUtil.createGenericResponse(cities, content);
    }

    @Transactional
    public CityDTO updateCityById(Long cityId, CityDTO cityDTO) {
        log.info("Start updating city with ID: {}", cityId);
        City foundCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, cityId));
        Country foundCountry = countryRepository.findById(cityDTO.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, cityDTO.getCountryId()));
        foundCity.setName(cityDTO.getName());
        foundCity.setCountry(foundCountry);
        cityRepository.save(foundCity);
        log.info("City with ID {} updated successfully.", cityId);
        return CityMapper.INSTANCE.entityToDTO(foundCity);
    }

    @Transactional
    public void deleteCityById(Long cityId) {
        log.info("Deleting city with ID: {}", cityId);
        City foundCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, cityId));
        cityRepository.delete(foundCity);
        log.info("City with ID {} deleted successfully.", cityId);
    }
}
