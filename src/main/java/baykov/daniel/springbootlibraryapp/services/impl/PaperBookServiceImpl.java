package baykov.daniel.springbootlibraryapp.services.impl;

import baykov.daniel.springbootlibraryapp.entities.Book;
import baykov.daniel.springbootlibraryapp.exceptions.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;
import baykov.daniel.springbootlibraryapp.repositories.PaperBookRepository;
import baykov.daniel.springbootlibraryapp.services.PaperBookService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaperBookServiceImpl implements PaperBookService {

    private final PaperBookRepository paperBookRepository;
    private final ModelMapper mapper;

    public PaperBookServiceImpl(PaperBookRepository paperBookRepository, ModelMapper mapper) {
        this.paperBookRepository = paperBookRepository;
        this.mapper = mapper;
    }

    @Override
    public BookDTO createPaperBook(BookDTO bookDTO) {
        Book book = mapToEntity(bookDTO);
        Book newBook = paperBookRepository.save(book);
        return mapToDTO(newBook);
    }

    @Override
    public BookDTO getPaperBookById(long id) {
        Book book = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return mapToDTO(book);
    }

    @Override
    public BookResponse getAllPaperBooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> allPaperBooks = paperBookRepository.findAll(pageable);

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
    public BookDTO updatePaperBook(BookDTO bookDTO, long id) {
        Book book = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());
        book.setDescription(bookDTO.getDescription());
        book.setBorrowedDate(bookDTO.getBorrowedDate());
        book.setNumberOfCopiesTotal(bookDTO.getNumberOfCopiesTotal());
        book.setNumberOfCopiesAvailable(bookDTO.getNumberOfCopiesAvailable());
        book.setISBN(bookDTO.getISBN());
        book.setBookType("PAPER");

        Book newBook = paperBookRepository.save(book);
        return mapToDTO(newBook);
    }

    @Override
    public void deletePaperBook(long id) {
        Book book = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        paperBookRepository.delete(book);
    }

    @Override
    public void updateNumberOfBooksAfterBorrowing(Long id) {
        Book book = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setNumberOfCopiesAvailable(book.getNumberOfCopiesAvailable() - 1);
    }

    @Override
    public void updateNumberOfBooksAfterReturning(Long id) {
        Book book = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setNumberOfCopiesAvailable(book.getNumberOfCopiesAvailable() + 1);
    }

    private Book mapToEntity(BookDTO bookDTO) {
        return mapper.map(bookDTO, Book.class);
    }

    private BookDTO mapToDTO(Book book) {
        return mapper.map(book, BookDTO.class);
    }
}
