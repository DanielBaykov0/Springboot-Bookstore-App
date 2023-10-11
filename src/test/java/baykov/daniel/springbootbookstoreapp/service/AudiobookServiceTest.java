package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Audiobook;
import baykov.daniel.springbootbookstoreapp.entity.Author;
import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.entity.Narrator;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.AudiobookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.AudiobookResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.AudiobookRepository;
import baykov.daniel.springbootbookstoreapp.repository.AuthorRepository;
import baykov.daniel.springbootbookstoreapp.repository.CategoryRepository;
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
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AudiobookServiceTest {

    private AudiobookService audiobookService;

    @Mock
    private AudiobookRepository audiobookRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private NarratorRepository narratorRepository;

    @Captor
    private ArgumentCaptor<Audiobook> audiobookArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<Integer> yearArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> titleArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> authorNameArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> descriptionArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> categoryArgumentCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @BeforeEach
    void setUp() {
        audiobookService = new AudiobookService(
                audiobookRepository, serviceUtil, authorRepository, categoryRepository, narratorRepository);
    }

    @Test
    void testCreateAudiobook_ReturnAudiobookResponseDTO() {
        List<Long> authorIDs = new ArrayList<>();
        List<Long> categoryIDs = new ArrayList<>();
        Narrator narrator = new Narrator();
        narrator.setId(1L);
        AudiobookRequestDTO audiobookRequestDTO = new AudiobookRequestDTO();
        audiobookRequestDTO.setAuthorIDs(authorIDs);
        audiobookRequestDTO.setCategoryIDs(categoryIDs);
        audiobookRequestDTO.setNarratorId(narrator.getId());

        List<Author> authors = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        when(authorRepository.findAllById(authorIDs)).thenReturn(authors);
        when(categoryRepository.findAllById(categoryIDs)).thenReturn(categories);
        when(narratorRepository.findById(narrator.getId())).thenReturn(Optional.of(narrator));

        Audiobook returnObject = new Audiobook();
        when(audiobookRepository.save(audiobookArgumentCaptor.capture())).thenReturn(returnObject);

        AudiobookResponseDTO createdAudiobook = audiobookService.createAudiobook(audiobookRequestDTO);
        Assertions.assertNotNull(createdAudiobook);

        Audiobook audiobook = audiobookArgumentCaptor.getValue();
        Assertions.assertNotNull(audiobook);
        Assertions.assertEquals(audiobook.getDuration(), audiobookRequestDTO.getDuration());
        Assertions.assertEquals(audiobook.getAuthors().stream().map(Author::getId).collect(Collectors.toList()), audiobookRequestDTO.getAuthorIDs());
        Assertions.assertEquals(audiobook.getCategories().stream().map(Category::getId).collect(Collectors.toList()), audiobookRequestDTO.getCategoryIDs());
        Assertions.assertEquals(audiobook.getNarrator().getId(), audiobookRequestDTO.getNarratorId());
        Assertions.assertEquals(audiobook.getFileSize(), audiobookRequestDTO.getFileSize());
        Assertions.assertEquals(audiobook.getFileFormat(), audiobookRequestDTO.getFileFormat());
    }

    @Test
    void testCreateAudiobook_ReturnNarratorException() {
        AudiobookRequestDTO audiobookRequestDTO = new AudiobookRequestDTO();
        audiobookRequestDTO.setNarratorId(1L);

        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> audiobookService.createAudiobook(audiobookRequestDTO)
        );

        Assertions.assertEquals("Narrator not found with ID : '1'", exception.getMessage());

        Long narratorId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(narratorId);
        Assertions.assertEquals(narratorId, audiobookRequestDTO.getNarratorId());
    }

    @Test
    void testGetAudiobookById_ReturnAudiobookResponseDTO() {
        Long audiobookId = 1L;
        List<Author> authors = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        Narrator narrator = new Narrator();
        narrator.setId(1L);

        Audiobook returnObject = new Audiobook();
        returnObject.setAuthors(authors);
        returnObject.setCategories(categories);
        returnObject.setNarrator(narrator);
        when(audiobookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        AudiobookResponseDTO retrievedAudiobook = audiobookService.getAudiobookById(audiobookId);
        Assertions.assertNotNull(retrievedAudiobook);

        Long capturedAudiobookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedAudiobookId);
        Assertions.assertEquals(capturedAudiobookId, audiobookId);
    }

    @Test
    void testGetAudiobookById_ReturnAudiobookException() {
        Long audiobookId = 1L;

        when(audiobookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> audiobookService.getAudiobookById(audiobookId)
        );

        Assertions.assertEquals("Audiobook not found with ID : '1'", exception.getMessage());

        Long capturedAudiobookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedAudiobookId);
        Assertions.assertEquals(capturedAudiobookId, audiobookId);
    }

    @Test
    void testGetAllAudiobooks_SortASC_ReturnAudiobookResponseDTO() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Audiobook> mockPage = new PageImpl<>(List.of());
        List<AudiobookResponseDTO> content = new ArrayList<>();
        GenericResponse<AudiobookResponseDTO> mockResponse = new GenericResponse<>();
        when(audiobookRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<AudiobookResponseDTO> retrievedAudiobookResponse = audiobookService.getAllAudiobooks(pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedAudiobookResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testUpdateAudiobookById_ReturnAudiobookResponseDTO() {
        Long audiobookId = 1L;
        List<Long> authorIDs = new ArrayList<>();
        List<Long> categoryIDs = new ArrayList<>();
        Narrator narrator = new Narrator();
        narrator.setId(1L);
        AudiobookRequestDTO audiobookRequestDTO = new AudiobookRequestDTO();
        audiobookRequestDTO.setAuthorIDs(authorIDs);
        audiobookRequestDTO.setCategoryIDs(categoryIDs);
        audiobookRequestDTO.setNarratorId(narrator.getId());

        Audiobook returnObject = new Audiobook();
        when(audiobookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));
        when(narratorRepository.findById(narrator.getId())).thenReturn(Optional.of(narrator));
        when(audiobookRepository.save(audiobookArgumentCaptor.capture())).thenReturn(returnObject);

        AudiobookResponseDTO updatedAudiobook = audiobookService.updateAudiobookById(audiobookId, audiobookRequestDTO);
        Assertions.assertNotNull(updatedAudiobook);

        Audiobook audiobook = audiobookArgumentCaptor.getValue();
        Assertions.assertNotNull(audiobook);
        Assertions.assertEquals(audiobook.getDuration(), audiobookRequestDTO.getDuration());
        Assertions.assertEquals(audiobook.getAuthors().stream().map(Author::getId).collect(Collectors.toList()), audiobookRequestDTO.getAuthorIDs());
        Assertions.assertEquals(audiobook.getCategories().stream().map(Category::getId).collect(Collectors.toList()), audiobookRequestDTO.getCategoryIDs());
        Assertions.assertEquals(audiobook.getNarrator().getId(), audiobookRequestDTO.getNarratorId());
        Assertions.assertEquals(audiobook.getFileSize(), audiobookRequestDTO.getFileSize());
        Assertions.assertEquals(audiobook.getFileFormat(), audiobookRequestDTO.getFileFormat());

        Long audiobookIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(audiobookIdCaptor);
        Assertions.assertEquals(audiobookIdCaptor, audiobookId);

        verify(audiobookRepository).save(audiobookArgumentCaptor.capture());
    }

    @Test
    void testUpdateAudiobookById_ReturnAudiobookException() {
        Long audiobookId = 1L;
        AudiobookRequestDTO audiobookRequestDTO = new AudiobookRequestDTO();

        when(audiobookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> audiobookService.updateAudiobookById(audiobookId, audiobookRequestDTO)
        );

        Assertions.assertEquals("Audiobook not found with ID : '1'", exception.getMessage());

        Long audiobookIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(audiobookIdCaptor);
        Assertions.assertEquals(audiobookIdCaptor, audiobookId);

        verify(audiobookRepository, never()).save(any());
    }

    @Test
    void testUpdateAudiobookById_ReturnNarratorException() {
        Long audiobookId = 1L;
        AudiobookRequestDTO audiobookRequestDTO = new AudiobookRequestDTO();
        audiobookRequestDTO.setNarratorId(1L);

        Audiobook returnObject = new Audiobook();
        when(audiobookRepository.findById(audiobookId)).thenReturn(Optional.of(returnObject));
        when(narratorRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> audiobookService.updateAudiobookById(audiobookId, audiobookRequestDTO)
        );

        Assertions.assertEquals("Narrator not found with ID : '1'", exception.getMessage());

        Long audiobookIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(audiobookIdCaptor);
        Assertions.assertEquals(audiobookIdCaptor, audiobookId);

        verify(audiobookRepository, never()).save(any());
    }

    @Test
    void testDeleteAudiobookById_Success() {
        Long audiobookId = 1L;

        Audiobook audiobookToDelete = new Audiobook();
        when(audiobookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(audiobookToDelete));

        audiobookService.deleteAudiobookById(audiobookId);

        Long capturedAudiobookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedAudiobookId);
        Assertions.assertEquals(capturedAudiobookId, audiobookId);

        verify(audiobookRepository).delete(audiobookToDelete);
    }

    @Test
    void testDeleteAudiobookById_ReturnAudiobookException() {
        Long audiobookId = 1L;

        when(audiobookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> audiobookService.deleteAudiobookById(audiobookId)
        );

        Assertions.assertEquals("Audiobook not found with ID : '1'", exception.getMessage());

        Long capturedAudiobookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedAudiobookId);
        Assertions.assertEquals(capturedAudiobookId, audiobookId);

        verify(audiobookRepository, never()).delete(any());
    }

    @Test
    void testGetSearchedAudiobooks_ReturnAudiobookResponseDTO() {
        String title = "title";
        String searchableTitle = "%" + title.toLowerCase().replace(" ", "%") + "%";
        String authorName = "authorName";
        String searchableAuthorName = "%" + authorName.toLowerCase().replace(" ", "%") + "%";
        String description = "description";
        String searchableDescription = "%" + description.toLowerCase().replace(" ", "%") + "%";
        String category = "category";
        String searchableCategory = "%" + category.toLowerCase().replace(" ", "%") + "%";
        Integer publicationYear = 2000;

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Audiobook> mockPage = new PageImpl<>(List.of());
        List<AudiobookResponseDTO> content = new ArrayList<>();
        GenericResponse<AudiobookResponseDTO> mockResponse = new GenericResponse<>();
        when(audiobookRepository.findBySearchedParams(
                titleArgumentCaptor.capture(),
                authorNameArgumentCaptor.capture(),
                descriptionArgumentCaptor.capture(),
                categoryArgumentCaptor.capture(),
                yearArgumentCaptor.capture(),
                pageableArgumentCaptor.capture()))
                .thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<AudiobookResponseDTO> retrievedAudiobookResponse =
                audiobookService.getSearchedAudiobooks(
                        title,
                        authorName,
                        description,
                        category,
                        publicationYear,
                        pageNo,
                        pageSize,
                        sortBy,
                        sortDir);
        Assertions.assertNotNull(retrievedAudiobookResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));

        String capturedSearchableTitle = titleArgumentCaptor.getValue();
        Assertions.assertEquals(searchableTitle, capturedSearchableTitle);

        String capturedSearchableAuthorName = authorNameArgumentCaptor.getValue();
        Assertions.assertEquals(searchableAuthorName, capturedSearchableAuthorName);

        String capturedSearchableDescription = descriptionArgumentCaptor.getValue();
        Assertions.assertEquals(searchableDescription, capturedSearchableDescription);

        String capturedSearchableCategory = categoryArgumentCaptor.getValue();
        Assertions.assertEquals(searchableCategory, capturedSearchableCategory);

        Integer capturedSearchableYear = yearArgumentCaptor.getValue();
        Assertions.assertEquals(publicationYear, capturedSearchableYear);
    }
}