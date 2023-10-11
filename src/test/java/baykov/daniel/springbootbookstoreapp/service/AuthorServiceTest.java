package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Author;
import baykov.daniel.springbootbookstoreapp.entity.City;
import baykov.daniel.springbootbookstoreapp.entity.Country;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.AuthorRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.AuthorResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.AuthorRepository;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Captor
    private ArgumentCaptor<Author> authorArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(
                authorRepository, countryRepository, cityRepository, serviceUtil);
    }

    @Test
    void testCreateAuthor_ReturnAuthorResponseDTO() {
        Country country = new Country("country");
        City city = new City("city", country);
        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO();
        authorRequestDTO.setCountryId(country.getId());
        authorRequestDTO.setCityId(city.getId());

        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));
        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));

        Author returnObject = new Author();
        when(authorRepository.save(authorArgumentCaptor.capture())).thenReturn(returnObject);

        AuthorResponseDTO createdAuthor = authorService.createAuthor(authorRequestDTO);
        Assertions.assertNotNull(createdAuthor);

        Author author = authorArgumentCaptor.getValue();
        Assertions.assertNotNull(author);
        Assertions.assertEquals(author.getFirstName(), authorRequestDTO.getFirstName());
        Assertions.assertEquals(author.getLastName(), authorRequestDTO.getLastName());
        Assertions.assertEquals(author.getCountry().getId(), authorRequestDTO.getCountryId());
        Assertions.assertEquals(author.getCity().getId(), authorRequestDTO.getCityId());
        Assertions.assertEquals(author.getBirthDate(), authorRequestDTO.getBirthDate());
        Assertions.assertEquals(author.getIsAlive(), authorRequestDTO.getIsAlive());
        Assertions.assertEquals(author.getDeathDate(), authorRequestDTO.getDeathDate());
    }

    @Test
    void testCreateAuthor_ReturnCountryException() {
        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO();
        authorRequestDTO.setCountryId(1L);

        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.createAuthor(authorRequestDTO)
        );

        Assertions.assertEquals("Country not found with ID : '1'", exception.getMessage());

        Long countryId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(countryId);
        Assertions.assertEquals(countryId, authorRequestDTO.getCountryId());
    }

    @Test
    void testCreateAuthor_ReturnCityException() {
        Country country = new Country("country");
        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO();
        authorRequestDTO.setCityId(1L);

        when(countryRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(country));
        when(cityRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.createAuthor(authorRequestDTO)
        );

        Assertions.assertEquals("City not found with ID : '1'", exception.getMessage());

        Long cityId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(cityId);
        Assertions.assertEquals(cityId, authorRequestDTO.getCityId());
    }

    @Test
    void testGetAuthorById_ReturnAuthorResponseDTO() {
        Long authorId = 1L;
        Country country = new Country("Country");
        country.setId(1L);
        City city = new City("city", country);
        city.setId(1L);

        Author returnObject = new Author();
        returnObject.setCountry(country);
        returnObject.setCity(city);
        when(authorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        AuthorResponseDTO retrievedAuthor = authorService.getAuthorById(authorId);
        Assertions.assertNotNull(retrievedAuthor);

        Long capturedAuthorId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedAuthorId);
        Assertions.assertEquals(capturedAuthorId, authorId);
    }

    @Test
    void testGetAuthorById_ReturnAuthorException() {
        Long authorId = 1L;

        when(authorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.getAuthorById(authorId)
        );

        Assertions.assertEquals("Author not found with ID : '1'", exception.getMessage());

        Long capturedAuthorId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedAuthorId);
        Assertions.assertEquals(capturedAuthorId, authorId);
    }

    @Test
    void testGetAllAuthors_SortASC_ReturnAuthorResponseDTO() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Author> mockPage = new PageImpl<>(List.of());
        List<AuthorResponseDTO> content = new ArrayList<>();
        GenericResponse<AuthorResponseDTO> mockResponse = new GenericResponse<>();
        when(authorRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<AuthorResponseDTO> retrievedAuthorResponse = authorService.getAllAuthors(pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedAuthorResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testUpdateAuthorById_ReturnAuthorResponseDTO() {
        Long authorId = 1L;
        Country country = new Country();
        country.setId(1L);
        City city = new City();
        city.setId(1L);
        AuthorRequestDTO updatedDto = new AuthorRequestDTO();
        updatedDto.setCountryId(country.getId());
        updatedDto.setCityId(city.getId());

        Author returnObject = new Author();
        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));
        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));
        when(authorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));
        when(authorRepository.save(authorArgumentCaptor.capture())).thenReturn(returnObject);

        AuthorResponseDTO updatedAuthor = authorService.updateAuthorById(updatedDto, authorId);
        Assertions.assertNotNull(updatedAuthor);

        Author author = authorArgumentCaptor.getValue();
        Assertions.assertNotNull(author);
        Assertions.assertEquals(author.getFirstName(), updatedDto.getFirstName());
        Assertions.assertEquals(author.getLastName(), updatedDto.getLastName());
        Assertions.assertEquals(author.getCountry().getId(), updatedDto.getCountryId());
        Assertions.assertEquals(author.getCity().getId(), updatedDto.getCityId());
        Assertions.assertEquals(author.getBirthDate(), updatedDto.getBirthDate());
        Assertions.assertEquals(author.getIsAlive(), updatedDto.getIsAlive());
        Assertions.assertEquals(author.getDeathDate(), updatedDto.getDeathDate());

        Long authorIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(authorIdCaptor);
        Assertions.assertEquals(authorIdCaptor, authorId);

        verify(authorRepository).save(authorArgumentCaptor.capture());
    }

    @Test
    void testUpdateAuthorById_ReturnAuthorException() {
        Long authorId = 1L;
        AuthorRequestDTO updatedDto = new AuthorRequestDTO();

        when(authorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.updateAuthorById(updatedDto, authorId)
        );

        Assertions.assertEquals("Author not found with ID : '1'", exception.getMessage());

        Long authorIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(authorIdCaptor);
        Assertions.assertEquals(authorIdCaptor, authorId);

        verify(authorRepository, never()).save(any());
    }

    @Test
    void testUpdateAuthorById_ReturnCountryException() {
        Long authorId = 1L;
        Country country = new Country();
        country.setId(1L);
        AuthorRequestDTO updatedDto = new AuthorRequestDTO();
        updatedDto.setCountryId(country.getId());

        Author returnObject = new Author();
        when(countryRepository.findById(country.getId())).thenReturn(Optional.empty());
        when(authorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.updateAuthorById(updatedDto, authorId)
        );

        Assertions.assertEquals("Country not found with ID : '1'", exception.getMessage());

        Long authorIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(authorIdCaptor);
        Assertions.assertEquals(authorIdCaptor, authorId);

        verify(authorRepository, never()).save(any());
    }

    @Test
    void testUpdateAuthorById_ReturnCityException() {
        Long authorId = 1L;
        Country country = new Country();
        country.setId(1L);
        City city = new City();
        city.setId(1L);
        AuthorRequestDTO updatedDto = new AuthorRequestDTO();
        updatedDto.setCountryId(country.getId());
        updatedDto.setCityId(city.getId());

        Author returnObject = new Author();
        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));
        when(cityRepository.findById(city.getId())).thenReturn(Optional.empty());
        when(authorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.updateAuthorById(updatedDto, authorId)
        );

        Assertions.assertEquals("City not found with ID : '1'", exception.getMessage());

        Long authorIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(authorIdCaptor);
        Assertions.assertEquals(authorIdCaptor, authorId);

        verify(authorRepository, never()).save(any());
    }

    @Test
    void testDeleteAuthorById_Success() {
        Long authorId = 1L;

        Author authorToDelete = new Author();
        when(authorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(authorToDelete));

        authorService.deleteAuthorById(authorId);

        Long capturedAuthorId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedAuthorId);
        Assertions.assertEquals(capturedAuthorId, authorId);

        verify(authorRepository).delete(authorToDelete);
    }

    @Test
    void testDeleteAuthorById_ReturnAuthorException() {
        Long authorId = 1L;

        when(authorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.deleteAuthorById(authorId)
        );

        Assertions.assertEquals("Author not found with ID : '1'", exception.getMessage());

        Long capturedAuthorId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedAuthorId);
        Assertions.assertEquals(capturedAuthorId, authorId);

        verify(authorRepository, never()).delete(any());
    }

    @Test
    void testGetSearchedAuthors_ReturnAuthorResponseDTO() {
        String name = "name";
        String searchableName = "%" + name.toLowerCase().replace(" ", "%") + "%";
        Long categoryId = 1L;
        Long cityId = 1L;

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Author> mockPage = new PageImpl<>(List.of());
        List<AuthorResponseDTO> content = new ArrayList<>();
        GenericResponse<AuthorResponseDTO> mockResponse = new GenericResponse<>();
        when(authorRepository.findBySearchParams(
                stringArgumentCaptor.capture(), eq(categoryId), eq(cityId), pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<AuthorResponseDTO> retrievedAuthorResponse =
                authorService.getSearchedAuthors(name, categoryId, cityId, pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedAuthorResponse);

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