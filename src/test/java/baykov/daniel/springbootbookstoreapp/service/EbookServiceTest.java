package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Author;
import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.entity.Ebook;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.EbookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.EbookResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.AuthorRepository;
import baykov.daniel.springbootbookstoreapp.repository.CategoryRepository;
import baykov.daniel.springbootbookstoreapp.repository.EbookRepository;
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
class EbookServiceTest {

    private EbookService ebookService;

    @Mock
    private EbookRepository ebookRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Captor
    private ArgumentCaptor<Ebook> ebookArgumentCaptor;

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
        ebookService = new EbookService(
                ebookRepository, serviceUtil, authorRepository, categoryRepository);
    }

    @Test
    void testCreateEbook_ReturnEbookResponseDTO() {
        List<Long> authorIDs = new ArrayList<>();
        List<Long> categoryIDs = new ArrayList<>();
        EbookRequestDTO ebookRequestDTO = new EbookRequestDTO();
        ebookRequestDTO.setAuthorIDs(authorIDs);
        ebookRequestDTO.setCategoryIDs(categoryIDs);

        List<Author> authors = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        when(authorRepository.findAllById(authorIDs)).thenReturn(authors);
        when(categoryRepository.findAllById(categoryIDs)).thenReturn(categories);

        Ebook returnObject = new Ebook();
        when(ebookRepository.save(ebookArgumentCaptor.capture())).thenReturn(returnObject);

        EbookResponseDTO createdEbook = ebookService.createEbook(ebookRequestDTO);
        Assertions.assertNotNull(createdEbook);

        Ebook ebook = ebookArgumentCaptor.getValue();
        Assertions.assertNotNull(ebook);
        Assertions.assertEquals(ebook.getNumberOfPages(), ebookRequestDTO.getNumberOfPages());
        Assertions.assertEquals(ebook.getAuthors().stream().map(Author::getId).collect(Collectors.toList()), ebookRequestDTO.getAuthorIDs());
        Assertions.assertEquals(ebook.getCategories().stream().map(Category::getId).collect(Collectors.toList()), ebookRequestDTO.getCategoryIDs());
        Assertions.assertEquals(ebook.getNumberOfAvailableCopies(), 1);
        Assertions.assertEquals(ebook.getFileSize(), ebookRequestDTO.getFileSize());
        Assertions.assertEquals(ebook.getFileFormat(), ebookRequestDTO.getFileFormat());
    }

    @Test
    void testGetEbookById_ReturnEbookResponseDTO() {
        Long ebookId = 1L;
        List<Author> authors = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        Ebook returnObject = new Ebook();
        returnObject.setAuthors(authors);
        returnObject.setCategories(categories);
        when(ebookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        EbookResponseDTO retrievedEbook = ebookService.getEbookById(ebookId);
        Assertions.assertNotNull(retrievedEbook);

        Long capturedEbookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedEbookId);
        Assertions.assertEquals(capturedEbookId, ebookId);
    }

    @Test
    void testGetEbookById_ReturnEbookException() {
        Long ebookId = 1L;

        when(ebookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> ebookService.getEbookById(ebookId)
        );

        Assertions.assertEquals("Ebook not found with ID : '1'", exception.getMessage());

        Long capturedEbookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedEbookId);
        Assertions.assertEquals(capturedEbookId, ebookId);
    }

    @Test
    void testGetAllEbooks_SortASC_ReturnEbookResponseDTO() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Ebook> mockPage = new PageImpl<>(List.of());
        List<EbookResponseDTO> content = new ArrayList<>();
        GenericResponse<EbookResponseDTO> mockResponse = new GenericResponse<>();
        when(ebookRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<EbookResponseDTO> retrievedEbookResponse = ebookService.getAllEbooks(pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedEbookResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testUpdateEbookById_ReturnEbookResponseDTO() {
        Long ebookId = 1L;
        List<Long> authorIDs = new ArrayList<>();
        List<Long> categoryIDs = new ArrayList<>();
        EbookRequestDTO ebookRequestDTO = new EbookRequestDTO();
        ebookRequestDTO.setAuthorIDs(authorIDs);
        ebookRequestDTO.setCategoryIDs(categoryIDs);

        Ebook returnObject = new Ebook();
        when(ebookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));
        when(ebookRepository.save(ebookArgumentCaptor.capture())).thenReturn(returnObject);

        EbookResponseDTO updatedEbook = ebookService.updateEBookById(ebookId, ebookRequestDTO);
        Assertions.assertNotNull(updatedEbook);

        Ebook ebook = ebookArgumentCaptor.getValue();
        Assertions.assertNotNull(ebook);
        Assertions.assertEquals(ebook.getNumberOfPages(), ebookRequestDTO.getNumberOfPages());
        Assertions.assertEquals(ebook.getAuthors().stream().map(Author::getId).collect(Collectors.toList()), ebookRequestDTO.getAuthorIDs());
        Assertions.assertEquals(ebook.getCategories().stream().map(Category::getId).collect(Collectors.toList()), ebookRequestDTO.getCategoryIDs());
        Assertions.assertEquals(ebook.getNumberOfAvailableCopies(), 1);
        Assertions.assertEquals(ebook.getFileSize(), ebookRequestDTO.getFileSize());
        Assertions.assertEquals(ebook.getFileFormat(), ebookRequestDTO.getFileFormat());

        Long ebookIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(ebookIdCaptor);
        Assertions.assertEquals(ebookIdCaptor, ebookId);

        verify(ebookRepository).save(ebookArgumentCaptor.capture());
    }

    @Test
    void testUpdateEbookById_ReturnEbookException() {
        Long ebook = 1L;
        EbookRequestDTO ebookRequestDTO = new EbookRequestDTO();

        when(ebookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> ebookService.updateEBookById(ebook, ebookRequestDTO)
        );

        Assertions.assertEquals("Ebook not found with ID : '1'", exception.getMessage());

        Long ebookIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(ebookIdCaptor);
        Assertions.assertEquals(ebookIdCaptor, ebook);

        verify(ebookRepository, never()).save(any());
    }

    @Test
    void testDeleteEbookById_Success() {
        Long ebookId = 1L;

        Ebook ebookToDelete = new Ebook();
        when(ebookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(ebookToDelete));

        ebookService.deleteEbookById(ebookId);

        Long capturedEbookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedEbookId);
        Assertions.assertEquals(capturedEbookId, ebookId);

        verify(ebookRepository).delete(ebookToDelete);
    }

    @Test
    void testDeleteEbookById_ReturnEbookException() {
        Long ebookId = 1L;

        when(ebookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> ebookService.deleteEbookById(ebookId)
        );

        Assertions.assertEquals("Ebook not found with ID : '1'", exception.getMessage());

        Long capturedEbookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedEbookId);
        Assertions.assertEquals(capturedEbookId, ebookId);

        verify(ebookRepository, never()).delete(any());
    }

    @Test
    void testGetSearchedEbooks_ReturnEbookResponseDTO() {
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

        Page<Ebook> mockPage = new PageImpl<>(List.of());
        List<EbookResponseDTO> content = new ArrayList<>();
        GenericResponse<EbookResponseDTO> mockResponse = new GenericResponse<>();
        when(ebookRepository.findBySearchedParams(
                titleArgumentCaptor.capture(),
                authorNameArgumentCaptor.capture(),
                descriptionArgumentCaptor.capture(),
                categoryArgumentCaptor.capture(),
                yearArgumentCaptor.capture(),
                pageableArgumentCaptor.capture()))
                .thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<EbookResponseDTO> retrievedEbookResponse =
                ebookService.getSearchedEbooks(
                        title,
                        authorName,
                        description,
                        category,
                        publicationYear,
                        pageNo,
                        pageSize,
                        sortBy,
                        sortDir);
        Assertions.assertNotNull(retrievedEbookResponse);

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