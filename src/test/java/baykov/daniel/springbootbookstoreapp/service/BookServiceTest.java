package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Author;
import baykov.daniel.springbootbookstoreapp.entity.Book;
import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.BookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.BookResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.AuthorRepository;
import baykov.daniel.springbootbookstoreapp.repository.BookRepository;
import baykov.daniel.springbootbookstoreapp.repository.CategoryRepository;
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

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.AVAILABLE_BOOKS_BIGGER_THAN_TOTAL;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;

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
        bookService = new BookService(
                bookRepository, serviceUtil, authorRepository, categoryRepository);
    }

    @Test
    void testCreateBook_ReturnBookResponseDTO() {
        List<Long> authorIDs = new ArrayList<>();
        List<Long> categoryIDs = new ArrayList<>();
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setAuthorIDs(authorIDs);
        bookRequestDTO.setCategoryIDs(categoryIDs);

        List<Author> authors = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        when(authorRepository.findAllById(authorIDs)).thenReturn(authors);
        when(categoryRepository.findAllById(categoryIDs)).thenReturn(categories);

        Book returnObject = new Book();
        when(bookRepository.save(bookArgumentCaptor.capture())).thenReturn(returnObject);

        BookResponseDTO createdBook = bookService.createBook(bookRequestDTO);
        Assertions.assertNotNull(createdBook);

        Book book = bookArgumentCaptor.getValue();
        Assertions.assertNotNull(book);
        Assertions.assertEquals(book.getNumberOfPages(), bookRequestDTO.getNumberOfPages());
        Assertions.assertEquals(book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()), bookRequestDTO.getAuthorIDs());
        Assertions.assertEquals(book.getCategories().stream().map(Category::getId).collect(Collectors.toList()), bookRequestDTO.getCategoryIDs());
        Assertions.assertEquals(book.getNumberOfAvailableCopies(), bookRequestDTO.getNumberOfAvailableCopies());
        Assertions.assertEquals(book.getNumberOfTotalCopies(), bookRequestDTO.getNumberOfAvailableCopies());
    }

    @Test
    void testGetBookById_ReturnBookResponseDTO() {
        Long bookId = 1L;
        List<Author> authors = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        Book returnObject = new Book();
        returnObject.setAuthors(authors);
        returnObject.setCategories(categories);
        when(bookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        BookResponseDTO retrievedBook = bookService.getBookById(bookId);
        Assertions.assertNotNull(retrievedBook);

        Long capturedBookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedBookId);
        Assertions.assertEquals(capturedBookId, bookId);
    }

    @Test
    void testGetBookById_ReturnBookException() {
        Long bookId = 1L;

        when(bookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.getBookById(bookId)
        );

        Assertions.assertEquals("Book not found with ID : '1'", exception.getMessage());

        Long capturedBookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedBookId);
        Assertions.assertEquals(capturedBookId, bookId);
    }

    @Test
    void testGetAllBooks_SortASC_ReturnBookResponseDTO() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Page<Book> mockPage = new PageImpl<>(List.of());
        List<BookResponseDTO> content = new ArrayList<>();
        GenericResponse<BookResponseDTO> mockResponse = new GenericResponse<>();
        when(bookRepository.findAll(pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<BookResponseDTO> retrievedBookResponse = bookService.getAllBooks(pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedBookResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testUpdateBookById_ReturnBookResponseDTO() {
        Long bookId = 1L;
        List<Long> authorIDs = new ArrayList<>();
        List<Long> categoryIDs = new ArrayList<>();
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setAuthorIDs(authorIDs);
        bookRequestDTO.setCategoryIDs(categoryIDs);
        bookRequestDTO.setNumberOfAvailableCopies(10);
        bookRequestDTO.setNumberOfTotalCopies(10);

        Book returnObject = new Book();
        when(bookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));
        when(bookRepository.save(bookArgumentCaptor.capture())).thenReturn(returnObject);

        BookResponseDTO updatedBook = bookService.updateBookById(bookId, bookRequestDTO);
        Assertions.assertNotNull(updatedBook);

        Book book = bookArgumentCaptor.getValue();
        Assertions.assertNotNull(book);
        Assertions.assertEquals(book.getNumberOfPages(), bookRequestDTO.getNumberOfPages());
        Assertions.assertEquals(book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()), bookRequestDTO.getAuthorIDs());
        Assertions.assertEquals(book.getCategories().stream().map(Category::getId).collect(Collectors.toList()), bookRequestDTO.getCategoryIDs());
        Assertions.assertEquals(book.getNumberOfAvailableCopies(), bookRequestDTO.getNumberOfAvailableCopies());
        Assertions.assertEquals(book.getNumberOfTotalCopies(), bookRequestDTO.getNumberOfTotalCopies());

        Long bookIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(bookIdCaptor);
        Assertions.assertEquals(bookIdCaptor, bookId);

        verify(bookRepository).save(bookArgumentCaptor.capture());
    }

    @Test
    void testUpdateBookById_ReturnAvailableBooksException() {
        Long bookId = 1L;
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setNumberOfAvailableCopies(11);
        bookRequestDTO.setNumberOfTotalCopies(10);

        Book returnObject = new Book();
        when(bookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(returnObject));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> bookService.updateBookById(bookId, bookRequestDTO)
        );

        Assertions.assertEquals(AVAILABLE_BOOKS_BIGGER_THAN_TOTAL, exception.getMessage());

        Long bookIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(bookIdCaptor);
        Assertions.assertEquals(bookIdCaptor, bookId);

        verify(bookRepository, never()).save(any());
    }

    @Test
    void testUpdateBookById_ReturnBookException() {
        Long bookId = 1L;
        BookRequestDTO bookRequestDTO = new BookRequestDTO();

        when(bookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.updateBookById(bookId, bookRequestDTO)
        );

        Assertions.assertEquals("Book not found with ID : '1'", exception.getMessage());

        Long bookIdCaptor = longArgumentCaptor.getValue();
        Assertions.assertNotNull(bookIdCaptor);
        Assertions.assertEquals(bookIdCaptor, bookId);

        verify(bookRepository, never()).save(any());
    }

    @Test
    void testDeleteBookById_Success() {
        Long bookId = 1L;

        Book bookToDelete = new Book();
        when(bookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(bookToDelete));

        bookService.deleteBookById(bookId);

        Long capturedBookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedBookId);
        Assertions.assertEquals(capturedBookId, bookId);

        verify(bookRepository).delete(bookToDelete);
    }

    @Test
    void testDeleteBookById_ReturnBookException() {
        Long bookId = 1L;

        when(bookRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.deleteBookById(bookId)
        );

        Assertions.assertEquals("Book not found with ID : '1'", exception.getMessage());

        Long capturedBookId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedBookId);
        Assertions.assertEquals(capturedBookId, bookId);

        verify(bookRepository, never()).delete(any());
    }

    @Test
    void testGetSearchedBooks_ReturnBookResponseDTO() {
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

        Page<Book> mockPage = new PageImpl<>(List.of());
        List<BookResponseDTO> content = new ArrayList<>();
        GenericResponse<BookResponseDTO> mockResponse = new GenericResponse<>();
        when(bookRepository.findBySearchedParams(
                titleArgumentCaptor.capture(),
                authorNameArgumentCaptor.capture(),
                descriptionArgumentCaptor.capture(),
                categoryArgumentCaptor.capture(),
                yearArgumentCaptor.capture(),
                pageableArgumentCaptor.capture()))
                .thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<BookResponseDTO> retrievedBookResponse =
                bookService.getSearchedBooks(
                        title,
                        authorName,
                        description,
                        category,
                        publicationYear,
                        pageNo,
                        pageSize,
                        sortBy,
                        sortDir);
        Assertions.assertNotNull(retrievedBookResponse);

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