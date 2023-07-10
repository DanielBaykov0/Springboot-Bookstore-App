package baykov.daniel.springbootlibraryapp.services.impl;

import baykov.daniel.springbootlibraryapp.entities.EBook;
import baykov.daniel.springbootlibraryapp.entities.EBookHistory;
import baykov.daniel.springbootlibraryapp.entities.User;
import baykov.daniel.springbootlibraryapp.exceptions.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.exceptions.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.EBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.payload.response.EBookHistoryResponse;
import baykov.daniel.springbootlibraryapp.repositories.EBookHistoryRepository;
import baykov.daniel.springbootlibraryapp.repositories.EBookRepository;
import baykov.daniel.springbootlibraryapp.repositories.UserRepository;
import baykov.daniel.springbootlibraryapp.services.EBookHistoryService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
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
public class EBookHistoryServiceImpl implements EBookHistoryService {

    private final EBookHistoryRepository eBookHistoryRepository;
    private final EBookRepository eBookRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public EBookHistoryServiceImpl(EBookHistoryRepository eBookHistoryRepository, EBookRepository eBookRepository, UserRepository userRepository, ModelMapper mapper) {
        this.eBookHistoryRepository = eBookHistoryRepository;
        this.eBookRepository = eBookRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public EBookHistoryDTO readEBook(Long userId, Long ebookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        EBook eBook = eBookRepository.findById(ebookId)
                .orElseThrow(() -> new ResourceNotFoundException("Ebook", "id", ebookId));

        EBookHistory eBookHistory = new EBookHistory();
        eBookHistory.setUser(user);
        eBookHistory.setEBook(eBook);
        eBookHistory.setRead(true);
        eBookHistory.setDownloaded(false);
        return mapToDTO(eBookHistoryRepository.save(eBookHistory));
    }

    @Override
    public EBookHistoryDTO downloadEBook(Long userId, Long ebookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        EBook eBook = eBookRepository.findById(ebookId)
                .orElseThrow(() -> new ResourceNotFoundException("Ebook", "id", ebookId));

        EBookHistory eBookHistory = new EBookHistory();
        eBookHistory.setUser(user);
        eBookHistory.setEBook(eBook);
        eBookHistory.setRead(false);
        eBookHistory.setDownloaded(true);
        return mapToDTO(eBookHistoryRepository.save(eBookHistory));
    }

    @Override
    public EBookHistoryDTO getUserAndEBook(Long userId, Long ebookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        EBook eBook = eBookRepository.findById(ebookId)
                .orElseThrow(() -> new ResourceNotFoundException("Ebook", "id", ebookId));
        EBookHistory eBookHistory = eBookHistoryRepository
                .findEBookByUserIdAndEBookId(user.getId(), eBook.getId())
                .orElseThrow(() -> new LibraryHTTPException(HttpStatus.BAD_REQUEST, AppConstants.INVALID_USER_AND_EBOOK_MESSAGE));
        return mapToDTO(eBookHistory);
    }

    @Override
    public EBookHistoryResponse getAllEBooksByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Page<EBookHistory> eBookHistories = eBookHistoryRepository.findAllEBooksByUserId(user.getId(), pageable);
        return getEBookHistoryResponse(eBookHistories);
    }

    private EBookHistoryResponse getEBookHistoryResponse(Page<EBookHistory> eBookHistories) {
        List<EBookHistory> eBookHistory = eBookHistories.getContent();
        List<EBookHistoryDTO>  content = eBookHistory.stream().map(this::mapToDTO).collect(Collectors.toList());
        EBookHistoryResponse eBookHistoryResponse = new EBookHistoryResponse();
        eBookHistoryResponse.setContent(content);
        eBookHistoryResponse.setPageNo(eBookHistories.getNumber());
        eBookHistoryResponse.setPageSize(eBookHistories.getSize());
        eBookHistoryResponse.setTotalPages(eBookHistories.getTotalPages());
        eBookHistoryResponse.setTotalElements(eBookHistories.getTotalElements());
        eBookHistoryResponse.setLast(eBookHistories.isLast());
        return eBookHistoryResponse;
    }

    private EBookHistoryDTO mapToDTO(EBookHistory eBookHistory) {
        return mapper.map(eBookHistory, EBookHistoryDTO.class);
    }
}