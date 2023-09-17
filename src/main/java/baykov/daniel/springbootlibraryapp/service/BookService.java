package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.constant.Messages;
import baykov.daniel.springbootlibraryapp.entity.Author;
import baykov.daniel.springbootlibraryapp.entity.Book;
import baykov.daniel.springbootlibraryapp.exception.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.BookMapper;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;
import baykov.daniel.springbootlibraryapp.repository.AuthorRepository;
import baykov.daniel.springbootlibraryapp.repository.BookRepository;
import baykov.daniel.springbootlibraryapp.service.helper.BookServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static baykov.daniel.springbootlibraryapp.constant.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookServiceHelper bookServiceHelper;
    private final AuthorRepository authorRepository;

    public BookDTO createBook(BookDTO bookDTO) {
        log.info("Creating book...");
        Book book = BookMapper.INSTANCE.dtoToEntity(bookDTO);
        bookRepository.save(book);
        log.info("Created book with ID: {}", book.getId());
        return BookMapper.INSTANCE.entityToDto(book);
    }

    public BookDTO getBookById(Long bookId) {
        log.info("Getting book by ID: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK, ID, bookId));
        log.info("Book with ID {} retrieved successfully", bookId);
        return BookMapper.INSTANCE.entityToDto(book);
    }

    public BookResponse getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving books...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> books = bookRepository.findAll(pageable);
        log.info("Retrieved {} books.", books.getContent().size());
        return bookServiceHelper.getBookResponse(books);
    }

    public BookDTO updateBookByBookId(Long bookId, BookDTO bookDTO) {
        log.info("Start updating book with ID: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK, ID, bookId));
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException(AUTHOR, ID, bookDTO.getAuthorId()));

        if (bookDTO.getNumberOfAvailableCopies() > bookDTO.getNumberOfTotalCopies()) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.AVAILABLE_BOOKS_BIGGER_THAN_TOTAL);
        }

        book.update(BookMapper.INSTANCE.dtoToEntity(bookDTO), author);
        bookRepository.save(book);
        log.info("Book with ID {} updated successfully.", bookId);
        return BookMapper.INSTANCE.entityToDto(book);
    }

    public void deleteBookByBookId(Long bookId) {
        log.info("Deleting book with ID: {}", bookId);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException(BOOK, ID, bookId));
        bookRepository.delete(book);
        log.info("Book with ID {} deleted successfully.", bookId);
    }

    public void updateNumberOfBooksAfterBorrowing(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK, ID, bookId));
        book.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies() - 1);
        log.info("Borrowed book with ID {} - New available copies: {}", bookId, book.getNumberOfAvailableCopies());
    }

    public void updateNumberOfBooksAfterReturning(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK, ID, bookId));
        book.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies() + 1);
        log.info("Returned book with ID {} - New available copies: {}", bookId, book.getNumberOfAvailableCopies());
    }

    public BookResponse getAllBooksByTitle(String title, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting books by title: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}", title, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> books = bookRepository.findAllByTitleContainingIgnoreCase(title, pageable);
        log.info("Found {} books matching the title.", books.getTotalElements());
        return bookServiceHelper.getBookResponse(books);
    }

    public BookResponse getAllBooksByAuthorName(String name, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting books by author name: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}", name, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        String searchedName = name != null ? "%" + name.trim().toLowerCase().replace(" ", "%") + "%" : null;

        Page<Book> books = bookRepository.findAllByAuthorName(searchedName, pageable);
        log.info("Found {} books matching the author name.", books.getTotalElements());
        return bookServiceHelper.getBookResponse(books);
    }

    public BookResponse getAllBooksByDescription(String description, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting books by description: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}", description, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> books = bookRepository.findAllByDescriptionContainingIgnoreCase(description, pageable);
        log.info("Found {} books matching the description.", books.getTotalElements());
        return bookServiceHelper.getBookResponse(books);
    }

    public BookResponse getAllBooksByGenre(String genre, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting books by genre: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}", genre, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> books = bookRepository.findAllByGenreContainingIgnoreCase(genre, pageable);
        log.info("Found {} books matching the genre.", books.getTotalElements());
        return bookServiceHelper.getBookResponse(books);
    }

    public BookResponse getAllBooksByPublicationYear(int publicationYear, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting books by publication year: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}", publicationYear, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> books = bookRepository.findAllByPublicationYear(publicationYear, pageable);
        log.info("Found {} books matching the publication year.", books.getTotalElements());
        return bookServiceHelper.getBookResponse(books);
    }
}
