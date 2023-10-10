package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.City;
import baykov.daniel.springbootbookstoreapp.entity.Country;
import baykov.daniel.springbootbookstoreapp.entity.Narrator;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.NarratorRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.NarratorResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.CityRepository;
import baykov.daniel.springbootbookstoreapp.repository.CountryRepository;
import baykov.daniel.springbootbookstoreapp.repository.NarratorRepository;
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
class NarratorServiceTest {

    private NarratorService narratorService;

    @Mock
    private NarratorRepository narratorRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Captor
    private ArgumentCaptor<Narrator> narratorArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @BeforeEach
    void setUp() {
        narratorService = new NarratorService(
                narratorRepository, serviceUtil, countryRepository, cityRepository);
    }

    @Test
    void testCreateNarrator_ReturnNarratorResponseDTO() {
        Country country = new Country("country");
        City city = new City("city", country);
        NarratorRequestDTO narratorRequestDTO = new NarratorRequestDTO();
        narratorRequestDTO.setCountryId(country.getId());
        narratorRequestDTO.setCityId(city.getId());

        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));
        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));

        Narrator returnObject = new Narrator();
        when(narratorRepository.save(narratorArgumentCaptor.capture())).thenReturn(returnObject);

        NarratorResponseDTO createdNarrator = narratorService.createNarrator(narratorRequestDTO);
        Assertions.assertNotNull(createdNarrator);

        Narrator narrator = narratorArgumentCaptor.getValue();
        Assertions.assertNotNull(narrator);
        Assertions.assertEquals(narrator.getFirstName(), narratorRequestDTO.getFirstName());
        Assertions.assertEquals(narrator.getLastName(), narratorRequestDTO.getLastName());
        Assertions.assertEquals(narrator.getCountry().getId(), narratorRequestDTO.getCountryId());
        Assertions.assertEquals(narrator.getCity().getId(), narratorRequestDTO.getCityId());
        Assertions.assertEquals(narrator.getBirthDate(), narratorRequestDTO.getBirthDate());
        Assertions.assertEquals(narrator.getIsAlive(), narratorRequestDTO.getIsAlive());
        Assertions.assertEquals(narrator.getDeathDate(), narratorRequestDTO.getDeathDate());
    }

    @Test
    void testCreateNarrator_ReturnCountryException() {
        NarratorRequestDTO narratorRequestDTO = new NarratorRequestDTO();
        narratorRequestDTO.setCountryId(1L);

        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> narratorService.createNarrator(narratorRequestDTO)
        );

        Assertions.assertEquals("Country not found with ID : '1'", exception.getMessage());

        Long countryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(countryId);
        Assertions.assertEquals(countryId, narratorRequestDTO.getCountryId());
    }

    @Test
    void testCreateNarrator_ReturnCityException() {
        Country country = new Country("country");
        NarratorRequestDTO narratorRequestDTO = new NarratorRequestDTO();
        narratorRequestDTO.setCityId(1L);

        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(country));
        when(cityRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> narratorService.createNarrator(narratorRequestDTO)
        );

        Assertions.assertEquals("City not found with ID : '1'", exception.getMessage());

        Long cityId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(cityId);
        Assertions.assertEquals(cityId, narratorRequestDTO.getCityId());
    }

    @Test
    void testGetNarratorById_ReturnNarratorResponseDTO() {
        Long narratorId = 1L;
        Country country = new Country("name");
        City city = new City("name", country);

        Narrator returnObject = new Narrator();
        returnObject.setCountry(country);
        returnObject.setCity(city);
        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        NarratorResponseDTO retrievedNarrator = narratorService.getNarratorById(narratorId);
        Assertions.assertNotNull(retrievedNarrator);

        Long capturedNarratorId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedNarratorId);
        Assertions.assertEquals(capturedNarratorId, narratorId);
    }

    @Test
    void testGetNarratorById_ReturnNarratorException() {
        Long narratorId = 1L;

        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> narratorService.getNarratorById(narratorId)
        );

        Assertions.assertEquals("Narrator not found with ID : '1'", exception.getMessage());

        Long capturedNarratorId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedNarratorId);
        Assertions.assertEquals(capturedNarratorId, narratorId);
    }

    @Test
    void testGetAllNarrators_SortASC_ReturnNarratorResponseDTO() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Narrator> mockPage = new PageImpl<>(List.of());
        List<NarratorResponseDTO> content = new ArrayList<>();
        GenericResponse<NarratorResponseDTO> mockResponse = new GenericResponse<>();
        when(narratorRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<NarratorResponseDTO> retrievedNarratorResponse = narratorService.getAllNarrators(pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedNarratorResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testUpdateNarratorById_ReturnNarratorResponseDTO() {
        Long narratorId = 1L;
        Country country = new Country();
        country.setId(1L);
        City city = new City();
        city.setId(1L);
        NarratorRequestDTO updatedDto = new NarratorRequestDTO();
        updatedDto.setCountryId(country.getId());
        updatedDto.setCityId(city.getId());

        Narrator returnObject = new Narrator();
        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));
        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));
        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));
        when(narratorRepository.save(narratorArgumentCaptor.capture())).thenReturn(returnObject);

        NarratorResponseDTO updatedNarrator = narratorService.updateNarratorById(narratorId, updatedDto);
        Assertions.assertNotNull(updatedNarrator);

        Narrator narrator = narratorArgumentCaptor.getValue();
        Assertions.assertNotNull(narrator);
        Assertions.assertEquals(narrator.getFirstName(), updatedDto.getFirstName());
        Assertions.assertEquals(narrator.getLastName(), updatedDto.getLastName());
        Assertions.assertEquals(narrator.getCountry().getId(), updatedDto.getCountryId());
        Assertions.assertEquals(narrator.getCity().getId(), updatedDto.getCityId());
        Assertions.assertEquals(narrator.getBirthDate(), updatedDto.getBirthDate());
        Assertions.assertEquals(narrator.getIsAlive(), updatedDto.getIsAlive());
        Assertions.assertEquals(narrator.getDeathDate(), updatedDto.getDeathDate());

        Long narratorIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(narratorIdCaptor);
        Assertions.assertEquals(narratorIdCaptor, narratorId);

        verify(narratorRepository).save(narratorArgumentCaptor.capture());
    }

    @Test
    void testUpdateNarratorById_ReturnNarratorException() {
        Long narratorId = 1L;
        NarratorRequestDTO updatedDto = new NarratorRequestDTO();

        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> narratorService.updateNarratorById(narratorId, updatedDto)
        );

        Assertions.assertEquals("Narrator not found with ID : '1'", exception.getMessage());

        Long narratorIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(narratorIdCaptor);
        Assertions.assertEquals(narratorIdCaptor, narratorId);

        verify(narratorRepository, never()).save(any());
    }

    @Test
    void testUpdateNarratorById_ReturnCountryException() {
        Long narratorId = 1L;
        Country country = new Country();
        country.setId(1L);
        NarratorRequestDTO updatedDto = new NarratorRequestDTO();
        updatedDto.setCountryId(country.getId());

        Narrator returnObject = new Narrator();
        when(countryRepository.findById(country.getId())).thenReturn(Optional.empty());
        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> narratorService.updateNarratorById(narratorId, updatedDto)
        );

        Assertions.assertEquals("Country not found with ID : '1'", exception.getMessage());

        Long narratorIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(narratorIdCaptor);
        Assertions.assertEquals(narratorIdCaptor, narratorId);

        verify(narratorRepository, never()).save(any());
    }

    @Test
    void testUpdateNarratorById_ReturnCityException() {
        Long narratorId = 1L;
        Country country = new Country();
        country.setId(1L);
        City city = new City();
        city.setId(1L);
        NarratorRequestDTO updatedDto = new NarratorRequestDTO();
        updatedDto.setCountryId(country.getId());
        updatedDto.setCityId(city.getId());

        Narrator returnObject = new Narrator();
        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));
        when(cityRepository.findById(city.getId())).thenReturn(Optional.empty());
        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> narratorService.updateNarratorById(narratorId, updatedDto)
        );

        Assertions.assertEquals("City not found with ID : '1'", exception.getMessage());

        Long narratorIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(narratorIdCaptor);
        Assertions.assertEquals(narratorIdCaptor, narratorId);

        verify(narratorRepository, never()).save(any());
    }

    @Test
    void testDeleteNarratorById_Success() {
        Long narratorId = 1L;

        Narrator narratorToDelete = new Narrator();
        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(narratorToDelete));

        narratorService.deleteNarratorById(narratorId);

        Long capturedNarratorId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedNarratorId);
        Assertions.assertEquals(capturedNarratorId, narratorId);

        verify(narratorRepository).delete(narratorToDelete);
    }

    @Test
    void testDeleteNarratorById_ReturnNarratorException() {
        Long narratorId = 1L;

        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> narratorService.deleteNarratorById(narratorId)
        );

        Assertions.assertEquals("Narrator not found with ID : '1'", exception.getMessage());

        Long capturedNarratorId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedNarratorId);
        Assertions.assertEquals(capturedNarratorId, narratorId);

        verify(narratorRepository, never()).delete(any());
    }

    @Test
    void testGetSearchedNarrators_ReturnNarratorResponseDTO() {
        String name = "name";
        String searchableName = "%" + name.toLowerCase().replace(" ", "%") + "%";
        Long categoryId = 1L;
        Long cityId = 1L;

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Narrator> mockPage = new PageImpl<>(List.of());
        List<NarratorResponseDTO> content = new ArrayList<>();
        GenericResponse<NarratorResponseDTO> mockResponse = new GenericResponse<>();
        when(narratorRepository.findBySearchParams(
                stringArgumentCaptor.capture(), eq(categoryId), eq(cityId), pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<NarratorResponseDTO> retrievedNarratorResponse =
                narratorService.getSearchedNarrators(name, categoryId, cityId, pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedNarratorResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));

        String capturedSearchableName = stringArgumentCaptor.getValue();
        Assertions.assertEquals(searchableName, capturedSearchableName);
    }
}