package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.City;
import baykov.daniel.springbootbookstoreapp.entity.Country;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.CityDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.CityRepository;
import baykov.daniel.springbootbookstoreapp.repository.CountryRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    private CityService cityService;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Captor
    private ArgumentCaptor<City> cityArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @BeforeEach
    void setUp() {
        cityService = new CityService(countryRepository, cityRepository, serviceUtil);
    }

    @Test
    void testCreateCity_ReturnCityDTO() {
        Country country = new Country("country");
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCountryId(country.getId());

        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));

        City returnObject = new City();
        when(cityRepository.save(cityArgumentCaptor.capture())).thenReturn(returnObject);

        CityDTO createdCity = cityService.createCity(cityDTO);
        Assertions.assertNotNull(createdCity);

        City city = cityArgumentCaptor.getValue();
        Assertions.assertNotNull(city);
        Assertions.assertEquals(city.getName(), cityDTO.getName());
        Assertions.assertEquals(city.getCountry().getId(), cityDTO.getCountryId());
    }

    @Test
    void testCreateCity_ReturnCountryException() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCountryId(1L);

        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> cityService.createCity(cityDTO)
        );

        Assertions.assertEquals("Country not found with ID : '1'", exception.getMessage());

        Long countryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(countryId);
        Assertions.assertEquals(countryId, cityDTO.getCountryId());
    }

    @Test
    void testGetCityById_ReturnCityDTO() {
        Long cityId = 1L;
        Country country = new Country("name");

        City returnObject = new City();
        returnObject.setCountry(country);
        when(cityRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        CityDTO retrievedCity = cityService.getCityById(cityId);
        Assertions.assertNotNull(retrievedCity);

        Long capturedCityId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCityId);
        Assertions.assertEquals(capturedCityId, cityId);
    }

    @Test
    void testGetCityById_ReturnCityException() {
        Long cityId = 1L;

        when(cityRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> cityService.getCityById(cityId)
        );

        Assertions.assertEquals("City not found with ID : '1'", exception.getMessage());

        Long capturedCityId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCityId);
        Assertions.assertEquals(capturedCityId, cityId);
    }

    @Test
    void testGetAllCities_SortASC_ReturnCityDTO() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<City> mockPage = new PageImpl<>(List.of());
        List<CityDTO> content = new ArrayList<>();
        GenericResponse<CityDTO> mockResponse = new GenericResponse<>();
        when(cityRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<CityDTO> retrievedCityResponse = cityService.getAllCities(pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedCityResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testGetAllCitiesByCountryName_SortASC_ReturnCityDTO() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";
        String countryName = "name";

        Page<City> mockPage = new PageImpl<>(List.of());
        List<CityDTO> content = new ArrayList<>();
        GenericResponse<CityDTO> mockResponse = new GenericResponse<>();
        when(cityRepository.findAllByCountryNameIgnoreCase(eq(countryName), pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<CityDTO> retrievedCityResponse = cityService.getAllCitiesByCountryName(countryName, pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedCityResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testUpdateCityById_ReturnCityDTO() {
        Long cityId = 1L;
        Country country = new Country();
        country.setId(1L);
        CityDTO updatedDto = new CityDTO();
        updatedDto.setCountryId(country.getId());

        City returnObject = new City();
        when(cityRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));
        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));
        when(cityRepository.save(cityArgumentCaptor.capture())).thenReturn(returnObject);

        CityDTO updatedCity = cityService.updateCityById(cityId, updatedDto);
        Assertions.assertNotNull(updatedCity);

        City city = cityArgumentCaptor.getValue();
        Assertions.assertNotNull(city);
        Assertions.assertEquals(city.getName(), updatedDto.getName());
        Assertions.assertEquals(city.getCountry().getId(), updatedDto.getCountryId());

        Long cityIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(cityIdCaptor);
        Assertions.assertEquals(cityIdCaptor, cityId);

        verify(cityRepository).save(cityArgumentCaptor.capture());
    }

    @Test
    void testUpdateCityById_ReturnCityException() {
        Long cityId = 1L;
        CityDTO updatedDto = new CityDTO();

        when(cityRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> cityService.updateCityById(cityId, updatedDto)
        );

        Assertions.assertEquals("City not found with ID : '1'", exception.getMessage());

        Long cityIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(cityIdCaptor);
        Assertions.assertEquals(cityIdCaptor, cityId);

        verify(cityRepository, never()).save(any());
    }

    @Test
    void testUpdateCityById_ReturnCountryException() {
        Long cityId = 1L;
        Country country = new Country();
        country.setId(1L);
        CityDTO updatedDto = new CityDTO();
        updatedDto.setCountryId(country.getId());

        City returnObject = new City();
        when(cityRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));
        when(countryRepository.findById(country.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> cityService.updateCityById(cityId, updatedDto)
        );

        Assertions.assertEquals("Country not found with ID : '1'", exception.getMessage());

        Long cityIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(cityIdCaptor);
        Assertions.assertEquals(cityIdCaptor, cityId);

        verify(cityRepository, never()).save(any());
    }

    @Test
    void testDeleteCityById_Success() {
        Long cityId = 1L;

        City cityToDelete = new City();
        when(cityRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(cityToDelete));

        cityService.deleteCityById(cityId);

        Long capturedCityId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCityId);
        Assertions.assertEquals(capturedCityId, cityId);

        verify(cityRepository).delete(cityToDelete);
    }

    @Test
    void testDeleteCityById_ReturnCityException() {
        Long cityId = 1L;

        when(cityRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> cityService.deleteCityById(cityId)
        );

        Assertions.assertEquals("City not found with ID : '1'", exception.getMessage());

        Long capturedCityId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCityId);
        Assertions.assertEquals(capturedCityId, cityId);

        verify(cityRepository, never()).delete(any());
    }
}