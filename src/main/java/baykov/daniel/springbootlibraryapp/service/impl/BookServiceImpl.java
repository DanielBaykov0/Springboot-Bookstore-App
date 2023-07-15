package baykov.daniel.springbootlibraryapp.service.impl;

import baykov.daniel.springbootlibraryapp.entity.Author;
import baykov.daniel.springbootlibraryapp.entity.Book;
import baykov.daniel.springbootlibraryapp.exception.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;
import baykov.daniel.springbootlibraryapp.repository.AuthorRepository;
import baykov.daniel.springbootlibraryapp.repository.BookRepository;
import baykov.daniel.springbootlibraryapp.service.BookService;
import baykov.daniel.springbootlibraryapp.utils.Messages;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ModelMapper mapper;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, ModelMapper mapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = mapToEntity(bookDTO);
        Book newBook = bookRepository.save(book);
        return mapToDTO(newBook);
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return mapToDTO(book);
    }

    @Override
    public BookResponse getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> allPaperBooks = bookRepository.findAll(pageable);

        List<Book> bookList = allPaperBooks.getContent();

        List<BookDTO> paperBooks = bookList.stream().map(this::mapToDTO).collect(Collectors.toList());
        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(paperBooks);
        bookResponse.setPageNo(allPaperBooks.getNumber());
        bookResponse.setPageSize(allPaperBooks.getSize());
        bookResponse.setTotalElements(allPaperBooks.getTotalElements());
        bookResponse.setTotalPages(allPaperBooks.getTotalPages());
        bookResponse.setLast(allPaperBooks.isLast());
        return bookResponse;
    }

    @Override
    public BookDTO updateBookByBookId(BookDTO bookDTO, Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", bookDTO.getAuthorId()));
        if (bookDTO.getNumberOfCopiesAvailable() > bookDTO.getNumberOfCopiesTotal()) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.AVAILABLE_BOOKS_BIGGER_THAN_TOTAL_MESSAGE);
        }

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(author);
        book.setGenre(bookDTO.getGenre());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setDescription(bookDTO.getDescription());
        book.setISBN(bookDTO.getISBN());
        book.setNumberOfCopiesTotal(bookDTO.getNumberOfCopiesTotal());
        book.setNumberOfCopiesAvailable(bookDTO.getNumberOfCopiesAvailable());
        book.setReadingLink(bookDTO.getReadingLink());
        book.setDownloadLink(bookDTO.getDownloadLink());
        return mapToDTO(bookRepository.save(book));
    }

    @Override
    public void deleteBookByBookId(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        bookRepository.delete(book);
    }

    @Override
    public void updateNumberOfBooksAfterBorrowing(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setNumberOfCopiesAvailable(book.getNumberOfCopiesAvailable() - 1);
    }

    @Override
    public void updateNumberOfBooksAfterReturning(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setNumberOfCopiesAvailable(book.getNumberOfCopiesAvailable() + 1);
    }

    @Override
    public BookResponse getBooksByTitleOrByGenreOrByDescriptionOrByPublicationYearOrByAuthorName(
            String title, String genre, String description, Long publicationYear, String authorFirstName, String authorLastName, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> content = bookRepository.findAllByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrDescriptionIgnoreCaseOrPublicationYearOrAuthorFirstNameIgnoreCaseOrAuthorLastNameIgnoreCase(
                title, genre, description, publicationYear, authorFirstName, authorFirstName, pageable);
        return getBookResponse(content);
    }

    private BookResponse getBookResponse(Page<Book> books) {
        List<Book> bookList = books.getContent();
        List<BookDTO> content = bookList.stream().map(this::mapToDTO).collect(Collectors.toList());
        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(content);
        bookResponse.setPageNo(books.getNumber());
        bookResponse.setPageSize(books.getSize());
        bookResponse.setTotalElements(books.getTotalElements());
        bookResponse.setPageSize(books.getSize());
        bookResponse.setLast(books.isLast());
        return bookResponse;
    }

    private Book mapToEntity(BookDTO bookDTO) {
        return mapper.map(bookDTO, Book.class);
    }

    private BookDTO mapToDTO(Book book) {
        return mapper.map(book, BookDTO.class);
    }
}
