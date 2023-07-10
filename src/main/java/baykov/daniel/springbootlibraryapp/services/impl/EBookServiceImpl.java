package baykov.daniel.springbootlibraryapp.services.impl;

import baykov.daniel.springbootlibraryapp.entities.EBook;
import baykov.daniel.springbootlibraryapp.exceptions.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.EBookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.EBookResponse;
import baykov.daniel.springbootlibraryapp.repositories.EBookRepository;
import baykov.daniel.springbootlibraryapp.services.EBookService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EBookServiceImpl implements EBookService {

    private final EBookRepository eBookRepository;
    private final ModelMapper mapper;

    public EBookServiceImpl(EBookRepository eBookRepository, ModelMapper mapper) {
        this.eBookRepository = eBookRepository;
        this.mapper = mapper;
    }

    @Override
    public EBookDTO createEBook(EBookDTO eBookDTO) {
        EBook eBook = mapToEntity(eBookDTO);
        EBook newEBook = eBookRepository.save(eBook);
        return mapToDTO(newEBook);
    }

    @Override
    public EBookDTO getEBookById(long id) {
        EBook eBook = eBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("EBook", "id", id));
        return mapToDTO(eBook);
    }

    @Override
    public EBookResponse getAllEBooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<EBook> allEBooks = eBookRepository.findAll(pageable);

        List<EBook> eBookList = allEBooks.getContent();

        List<EBookDTO> eBooks = eBookList.stream().map(this::mapToDTO).collect(Collectors.toList());
        EBookResponse eBookResponse = new EBookResponse();
        eBookResponse.setContent(eBooks);
        eBookResponse.setPageNo(allEBooks.getNumber());
        eBookResponse.setPageSize(allEBooks.getSize());
        eBookResponse.setTotalElements(allEBooks.getTotalElements());
        eBookResponse.setTotalPages(allEBooks.getTotalPages());
        eBookResponse.setLast(allEBooks.isLast());
        return eBookResponse;
    }

    @Override
    public EBookDTO updateEBook(EBookDTO eBookDTO, long id) {
        EBook eBook = eBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("EBook", "id", id));
        eBook.setTitle(eBookDTO.getBookTitle());
        eBook.setAuthor(eBookDTO.getBookAuthor());
        eBook.setGenre(eBookDTO.getBookGenre());
        eBook.setDescription(eBookDTO.getBookDescription());
        eBook.setDownloadLink(eBookDTO.getEBookDownloadLink());
        eBook.setReadingLink(eBookDTO.getEBookReadOnlineLink());
        eBook.setISBN(eBookDTO.getISBN());
        eBook.setBookType("EBOOK");
        return mapToDTO(eBook);
    }

    @Override
    public void deleteEBook(long id) {
        EBook eBook = eBookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("EBook", "id", id));
        eBookRepository.delete(eBook);
    }

    private EBook mapToEntity(EBookDTO eBookDTO) {
        return mapper.map(eBookDTO, EBook.class);
    }

    private EBookDTO mapToDTO(EBook eBook) {
        return mapper.map(eBook, EBookDTO.class);
    }
}
