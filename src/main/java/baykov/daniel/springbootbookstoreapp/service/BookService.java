package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Author;
import baykov.daniel.springbootbookstoreapp.entity.Book;
import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.BookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.BookResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.BookMapper;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.AuthorRepository;
import baykov.daniel.springbootbookstoreapp.repository.BookRepository;
import baykov.daniel.springbootbookstoreapp.repository.CategoryRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.AVAILABLE_BOOKS_BIGGER_THAN_TOTAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ServiceUtil serviceUtil;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        log.info("Creating book...");

        List<Author> authors = authorRepository.findAllById(bookRequestDTO.getCategoryIDs());
        serviceUtil.validateIDs(bookRequestDTO.getAuthorIDs(), authors, AUTHOR);
        log.debug("Fetched authors: {}", authors);

        List<Category> categories = categoryRepository.findAllById(bookRequestDTO.getCategoryIDs());
        serviceUtil.validateIDs(bookRequestDTO.getCategoryIDs(), categories, CATEGORY);
        log.debug("Fetched categories: {}", categories);

        Book book = new Book(
                BookMapper.INSTANCE.dtoToEntity(bookRequestDTO),
                authors,
                categories);

        bookRepository.save(book);
        log.info("Created book with ID: {}", book.getId());
        return BookMapper.INSTANCE.entityToDTO(book);
    }

    public BookResponseDTO getBookById(Long bookId) {
        log.info("Getting book by ID: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK, ID, bookId));
        log.info("Book with ID {} retrieved successfully.", bookId);
        return BookMapper.INSTANCE.entityToDTO(book);
    }

    public GenericResponse<BookResponseDTO> getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving books...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> books = bookRepository.findAll(pageable);
        List<BookResponseDTO> content = BookMapper.INSTANCE.entityToDTO(books.getContent());
        log.info("Retrieved {} books.", books.getContent().size());
        return serviceUtil.createGenericResponse(books, content);
    }

    @Transactional
    public BookResponseDTO updateBookById(Long bookId, BookRequestDTO bookRequestDTO) {
        log.info("Start updating book with ID: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK, ID, bookId));
        log.debug("Fetched book: {}", book);

        if (bookRequestDTO.getNumberOfAvailableCopies() > bookRequestDTO.getNumberOfTotalCopies()) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AVAILABLE_BOOKS_BIGGER_THAN_TOTAL);
        }

        List<Author> authors = null;
        if (bookRequestDTO.getAuthorIDs() != null) {
            authors = authorRepository.findAllById(bookRequestDTO.getAuthorIDs());
            serviceUtil.validateIDs(bookRequestDTO.getAuthorIDs(), authors, AUTHOR);
        }
        log.debug("Fetched authors: {}", authors);

        List<Category> categories = null;
        if (bookRequestDTO.getCategoryIDs() != null) {
            categories = categoryRepository.findAllById(bookRequestDTO.getCategoryIDs());
            serviceUtil.validateIDs(bookRequestDTO.getCategoryIDs(), categories, CATEGORY);
        }
        log.debug("Fetched categories: {}", categories);

        book.update(BookMapper.INSTANCE.dtoToEntity(bookRequestDTO), authors, categories);
        bookRepository.save(book);
        log.info("Book with ID {} updated successfully.", bookId);
        return BookMapper.INSTANCE.entityToDTO(book);
    }

    @Transactional
    public void deleteBookById(Long bookId) {
        log.info("Deleting book with ID: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK, ID, bookId));
        bookRepository.delete(book);
        log.info("Book with ID {} deleted successfully.", bookId);
    }

    public GenericResponse<BookResponseDTO> getSearchedBooks(String title, String authorName, String description, String category, Integer publicationYear, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting books by title: {}, authorName: {}, description: {}, category: {}, publicationYear: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                title, authorName, description, category, publicationYear, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        String searchableTitle = title != null ? "%" + title.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableAuthorName = authorName != null ? "%" + authorName.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableDescription = description != null ? "%" + description.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableCategory = category != null ? "%" + category.trim().toLowerCase().replace(" ", "%") + "%" : null;

        Page<Book> books = bookRepository
                .findBySearchedParams(searchableTitle, searchableAuthorName, searchableDescription, searchableCategory, publicationYear, pageable);
        List<BookResponseDTO> content = BookMapper.INSTANCE.entityToDTO(books.getContent());
        log.info("Found {} books matching the params.", books.getTotalElements());
        return serviceUtil.createGenericResponse(books, content);
    }
}
