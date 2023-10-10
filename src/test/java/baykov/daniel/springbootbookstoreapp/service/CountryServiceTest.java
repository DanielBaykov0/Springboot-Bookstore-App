package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Country;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.CountryDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    private CountryService countryService;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Captor
    private ArgumentCaptor<Country> countryArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @BeforeEach
    void setUp() {
        countryService = new CountryService(countryRepository, serviceUtil);
    }

    @Test
    void testCreateCountry_ReturnCountryDTO() {
        CountryDTO countryDTO = new CountryDTO();

        Country returnObject = new Country();
        when(countryRepository.save(countryArgumentCaptor.capture())).thenReturn(returnObject);

        CountryDTO createdCountry = countryService.createCountry(countryDTO);
        Assertions.assertNotNull(createdCountry);

        Country country = countryArgumentCaptor.getValue();
        Assertions.assertNotNull(country);
        Assertions.assertEquals(country.getName(), countryDTO.getName());
    }

    @Test
    void testGetCountryById_ReturnCountryDTO() {
        Long countryId = 1L;

        Country returnObject = new Country();
        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        CountryDTO retrievedCountry = countryService.getCountryById(countryId);
        Assertions.assertNotNull(retrievedCountry);

        Long capturedCountryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCountryId);
        Assertions.assertEquals(capturedCountryId, countryId);
    }

    @Test
    void testGetCountryById_ReturnCountryException() {
        Long countryId = 1L;

        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> countryService.getCountryById(countryId)
        );

        Assertions.assertEquals("Country not found with ID : '1'", exception.getMessage());

        Long capturedCountryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCountryId);
        Assertions.assertEquals(capturedCountryId, countryId);
    }

    @Test
    void testGetAllCountries_SortASC_ReturnCountryDTO() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Country> mockPage = new PageImpl<>(List.of());
        List<CountryDTO> content = new ArrayList<>();
        GenericResponse<CountryDTO> mockResponse = new GenericResponse<>();
        when(countryRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<CountryDTO> retrievedCountryResponse = countryService.getAllCountries(pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedCountryResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testUpdateCountryById_ReturnCountryDTO() {
        Long countryId = 1L;
        CountryDTO updatedDto = new CountryDTO();

        Country returnObject = new Country();
        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));
        when(countryRepository.save(countryArgumentCaptor.capture())).thenReturn(returnObject);

        CountryDTO updatedCountry = countryService.updateCountryById(countryId, updatedDto);
        Assertions.assertNotNull(updatedCountry);

        Country country = countryArgumentCaptor.getValue();
        Assertions.assertNotNull(country);
        Assertions.assertEquals(country.getName(), updatedDto.getName());

        Long countryIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(countryIdCaptor);
        Assertions.assertEquals(countryIdCaptor, countryId);

        verify(countryRepository).save(countryArgumentCaptor.capture());
    }

    @Test
    void testUpdateCountryById_ReturnCountryException() {
        Long countryId = 1L;
        CountryDTO updatedDto = new CountryDTO();

        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> countryService.updateCountryById(countryId, updatedDto)
        );

        Assertions.assertEquals("Country not found with ID : '1'", exception.getMessage());

        Long countryIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(countryIdCaptor);
        Assertions.assertEquals(countryIdCaptor, countryId);

        verify(countryRepository, never()).save(any());
    }

    @Test
    void testDeleteCountryById_Success() {
        Long countryId = 1L;

        Country countryToDelete = new Country();
        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(countryToDelete));

        countryService.deleteCountryById(countryId);

        Long capturedCountryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCountryId);
        Assertions.assertEquals(capturedCountryId, countryId);

        verify(countryRepository).delete(countryToDelete);
    }

    @Test
    void testDeleteCountryById_ReturnCountryException() {
        Long countryId = 1L;

        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> countryService.deleteCountryById(countryId)
        );

        Assertions.assertEquals("Country not found with ID : '1'", exception.getMessage());

        Long capturedCountryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCountryId);
        Assertions.assertEquals(capturedCountryId, countryId);

        verify(countryRepository, never()).delete(any());
    }
}