package baykov.daniel.springbootlibraryapp.services.impl;

import baykov.daniel.springbootlibraryapp.entities.PaperBook;
import baykov.daniel.springbootlibraryapp.exceptions.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.PaperBookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.PaperBookResponse;
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
    public PaperBookDTO createPaperBook(PaperBookDTO paperBookDTO) {
        PaperBook paperBook = mapToEntity(paperBookDTO);
        PaperBook newPaperBook = paperBookRepository.save(paperBook);
        return mapToDTO(newPaperBook);
    }

    @Override
    public PaperBookDTO getPaperBookById(long id) {
        PaperBook paperBook = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return mapToDTO(paperBook);
    }

    @Override
    public PaperBookResponse getAllPaperBooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<PaperBook> allPaperBooks = paperBookRepository.findAll(pageable);

        List<PaperBook> paperBookList = allPaperBooks.getContent();

        List<PaperBookDTO> paperBooks = paperBookList.stream().map(this::mapToDTO).collect(Collectors.toList());
        PaperBookResponse paperBookResponse = new PaperBookResponse();
        paperBookResponse.setContent(paperBooks);
        paperBookResponse.setPageNo(allPaperBooks.getNumber());
        paperBookResponse.setPageSize(allPaperBooks.getSize());
        paperBookResponse.setTotalElements(allPaperBooks.getTotalElements());
        paperBookResponse.setTotalPages(allPaperBooks.getTotalPages());
        paperBookResponse.setLast(allPaperBooks.isLast());
        return paperBookResponse;
    }

    @Override
    public PaperBookDTO updatePaperBook(PaperBookDTO paperBookDTO, long id) {
        PaperBook paperBook = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        paperBook.setBookTitle(paperBookDTO.getBookTitle());
        paperBook.setBookAuthor(paperBookDTO.getBookAuthor());
        paperBook.setBookGenre(paperBookDTO.getBookGenre());
        paperBook.setBookDescription(paperBookDTO.getBookDescription());
        paperBook.setBorrowedDate(paperBookDTO.getBorrowedDate());
        paperBook.setPaperBookNumberOfCopiesTotal(paperBookDTO.getPaperBookNumberOfCopiesTotal());
        paperBook.setPaperBookNumberOfCopiesAvailable(paperBookDTO.getPaperBookNumberOfCopiesAvailable());
        paperBook.setISBN(paperBookDTO.getISBN());
        paperBook.setBookType("PAPER");

        PaperBook newPaperBook = paperBookRepository.save(paperBook);
        return mapToDTO(newPaperBook);
    }

    @Override
    public void deletePaperBook(long id) {
        PaperBook paperBook = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        paperBookRepository.delete(paperBook);
    }

    @Override
    public void updateNumberOfBooksAfterBorrowing(Long id) {
        PaperBook paperBook = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        paperBook.setPaperBookNumberOfCopiesAvailable(paperBook.getPaperBookNumberOfCopiesAvailable() - 1);
    }

    @Override
    public void updateNumberOfBooksAfterReturning(Long id) {
        PaperBook paperBook = paperBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        paperBook.setPaperBookNumberOfCopiesAvailable(paperBook.getPaperBookNumberOfCopiesAvailable() + 1);
    }

    private PaperBook mapToEntity(PaperBookDTO paperBookDTO) {
        return mapper.map(paperBookDTO, PaperBook.class);
    }

    private PaperBookDTO mapToDTO(PaperBook paperBook) {
        return mapper.map(paperBook, PaperBookDTO.class);
    }
}
