package baykov.daniel.springbootlibraryapp.service.impl;

import baykov.daniel.springbootlibraryapp.entity.Book;
import baykov.daniel.springbootlibraryapp.entity.User;
import baykov.daniel.springbootlibraryapp.entity.UserBookHistory;
import baykov.daniel.springbootlibraryapp.exception.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.UserBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.payload.response.UserBookHistoryResponse;
import baykov.daniel.springbootlibraryapp.repository.UserBookHistoryRepository;
import baykov.daniel.springbootlibraryapp.repository.BookRepository;
import baykov.daniel.springbootlibraryapp.repository.UserRepository;
import baykov.daniel.springbootlibraryapp.service.UserBookHistoryService;
import baykov.daniel.springbootlibraryapp.service.BookService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import baykov.daniel.springbootlibraryapp.utils.Messages;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserBookHistoryServiceImpl implements UserBookHistoryService {

    private final UserBookHistoryRepository userBookHistoryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final ModelMapper mapper;

    public UserBookHistoryServiceImpl(UserBookHistoryRepository userBookHistoryRepository, UserRepository userRepository, BookRepository bookRepository, BookService bookService, ModelMapper mapper) {
        this.userBookHistoryRepository = userBookHistoryRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @Override
    public UserBookHistoryDTO borrowBookById(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<UserBookHistory> userBookHistoryList = userBookHistoryRepository.findByUser(user);
        userBookHistoryList.stream()
                .filter(record -> record.getReturnDateTime().isBefore(LocalDateTime.now()) && !record.isReturned())
                .findAny()
                .ifPresent(record -> {
                    throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.PENDING_RETURN_MESSAGE);
                });

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        if (book.getNumberOfCopiesAvailable() < 1) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.NO_BOOKS_AVAILABLE_MESSAGE);
        }

        UserBookHistory userBookHistory = new UserBookHistory();
        userBookHistory.setBook(book);
        userBookHistory.setUser(user);
        userBookHistory.setBorrowDateTime(LocalDateTime.now());
        userBookHistory.setReturnDateTime(LocalDateTime.now().plusDays(AppConstants.DEFAULT_DAYS_TO_RETURN_A_BOOK));
        userBookHistory.setReturned(false);
        userBookHistory.setRead(false);
        userBookHistory.setDownloaded(false);

        bookService.updateNumberOfBooksAfterBorrowing(bookId);
        return mapToDTO(userBookHistoryRepository.save(userBookHistory));
    }

    @Override
    public UserBookHistoryDTO returnBookByHistoryId(Long userId, Long userBookHistoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        UserBookHistory userBookHistory = userBookHistoryRepository.findById(userBookHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow History", "id", userBookHistoryId));

        if (!Objects.equals(userBookHistory.getUser().getId(), user.getId()))
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.NO_VALID_BOOK_USER_MESSAGE);

        if (userBookHistory.isReturned()) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.BOOK_ALREADY_RETURNED_MESSAGE);
        }

        userBookHistory.setReturned(true);
        bookService.updateNumberOfBooksAfterReturning(userBookHistory.getBook().getId());
        return mapToDTO(userBookHistoryRepository.save(userBookHistory));
    }

    @Override
    public UserBookHistoryDTO postponeReturnDateByHistoryId(Long userId, Long userBookHistoryId, Long days) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        UserBookHistory userBookHistory = userBookHistoryRepository.findById(userBookHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow History", "id", userBookHistoryId));

        if (!Objects.equals(userBookHistory.getUser().getId(), user.getId())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.NO_VALID_BOOK_USER_MESSAGE);
        }

        if (userBookHistory.isReturned()) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.BOOK_ALREADY_RETURNED_MESSAGE);
        }

        if (days < 1) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.INVALID_POSTPONE_DAYS_MESSAGE);
        }

        if (ChronoUnit.DAYS.between(userBookHistory.getBorrowDateTime(),
                userBookHistory.getReturnDateTime().plusDays(days)) > AppConstants.DEFAULT_MAX_POSTPONE_DAYS) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.LIMIT_POSTPONE_DAYS_MESSAGE);
        }

        userBookHistory.setReturnDateTime(userBookHistory.getReturnDateTime().plusDays(days));
        return mapToDTO(userBookHistoryRepository.save(userBookHistory));
    }

    @Override
    public UserBookHistoryDTO readBookById(Long userId, Long ebookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Book book = bookRepository.findById(ebookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", ebookId));

        UserBookHistory userBookHistory = new UserBookHistory();
        userBookHistory.setBook(book);
        userBookHistory.setUser(user);
        userBookHistory.setBorrowDateTime(null);
        userBookHistory.setReturnDateTime(null);
        userBookHistory.setReturned(false);
        userBookHistory.setRead(true);
        userBookHistory.setDownloaded(false);
        return mapToDTO(userBookHistoryRepository.save(userBookHistory));
    }

    @Override
    public UserBookHistoryDTO downloadBookById(Long userId, Long ebookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Book book = bookRepository.findById(ebookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", ebookId));

        UserBookHistory userBookHistory = new UserBookHistory();
        userBookHistory.setBook(book);
        userBookHistory.setUser(user);
        userBookHistory.setBorrowDateTime(null);
        userBookHistory.setReturnDateTime(null);
        userBookHistory.setReturned(false);
        userBookHistory.setRead(false);
        userBookHistory.setDownloaded(true);
        return mapToDTO(userBookHistoryRepository.save(userBookHistory));
    }

    @Override
    public UserBookHistoryDTO getUserAndBook(Long userId, Long ebookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Book book = bookRepository.findById(ebookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", ebookId));
        UserBookHistory userBookHistory = userBookHistoryRepository
                .findEBookByUserIdAndBookId(user.getId(), book.getId())
                .orElseThrow(() -> new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.INVALID_USER_AND_BOOK_MESSAGE));
        return mapToDTO(userBookHistory);
    }

    @Override
    public UserBookHistoryResponse getAllBooksByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Page<UserBookHistory> eBookHistories = userBookHistoryRepository.findAllEBooksByUserId(user.getId(), pageable);
        return getUserBookHistoryResponse(eBookHistories);
    }

    private UserBookHistoryResponse getUserBookHistoryResponse(Page<UserBookHistory> userBookHistories) {
        List<UserBookHistory> userBookHistory = userBookHistories.getContent();
        List<UserBookHistoryDTO>  content = userBookHistory.stream().map(this::mapToDTO).collect(Collectors.toList());
        UserBookHistoryResponse userBookHistoryResponse = new UserBookHistoryResponse();
        userBookHistoryResponse.setContent(content);
        userBookHistoryResponse.setPageNo(userBookHistories.getNumber());
        userBookHistoryResponse.setPageSize(userBookHistories.getSize());
        userBookHistoryResponse.setTotalPages(userBookHistories.getTotalPages());
        userBookHistoryResponse.setTotalElements(userBookHistories.getTotalElements());
        userBookHistoryResponse.setLast(userBookHistories.isLast());
        return userBookHistoryResponse;
    }

    private UserBookHistoryDTO mapToDTO(UserBookHistory userBookHistory) {
        return mapper.map(userBookHistory, UserBookHistoryDTO.class);
    }
}
