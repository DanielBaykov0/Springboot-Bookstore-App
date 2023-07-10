package baykov.daniel.springbootlibraryapp.services.impl;

import baykov.daniel.springbootlibraryapp.entities.BorrowPaperBookHistory;
import baykov.daniel.springbootlibraryapp.entities.PaperBook;
import baykov.daniel.springbootlibraryapp.entities.User;
import baykov.daniel.springbootlibraryapp.exceptions.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.exceptions.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.BorrowPaperBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.repositories.BorrowPaperBookHistoryRepository;
import baykov.daniel.springbootlibraryapp.repositories.PaperBookRepository;
import baykov.daniel.springbootlibraryapp.repositories.UserRepository;
import baykov.daniel.springbootlibraryapp.services.BorrowPaperBookHistoryService;
import baykov.daniel.springbootlibraryapp.services.PaperBookService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class BorrowPaperBookHistoryServiceImpl implements BorrowPaperBookHistoryService {

    private final BorrowPaperBookHistoryRepository borrowPaperBookHistoryRepository;
    private final UserRepository userRepository;
    private final PaperBookRepository paperBookRepository;
    private final PaperBookService paperBookService;
    private final ModelMapper mapper;

    public BorrowPaperBookHistoryServiceImpl(BorrowPaperBookHistoryRepository borrowPaperBookHistoryRepository, UserRepository userRepository, PaperBookRepository paperBookRepository, PaperBookService paperBookService, ModelMapper mapper) {
        this.borrowPaperBookHistoryRepository = borrowPaperBookHistoryRepository;
        this.userRepository = userRepository;
        this.paperBookRepository = paperBookRepository;
        this.paperBookService = paperBookService;
        this.mapper = mapper;
    }

    @Override
    public BorrowPaperBookHistoryDTO borrowPaperBookById(Long bookId) {
        User loggedUser = getLoggedUser();

        List<BorrowPaperBookHistory> borrowPaperBookHistoryList = borrowPaperBookHistoryRepository.findByUser(loggedUser);
        borrowPaperBookHistoryList.stream()
                .filter(record -> record.getReturnDateTime().isBefore(LocalDateTime.now()) && !record.isReturned())
                .findAny()
                .ifPresent(record -> {
                    throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AppConstants.PENDING_RETURN_MESSAGE);
                });

        PaperBook paperBook = paperBookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        if (paperBook.getPaperBookNumberOfCopiesAvailable() < 1) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AppConstants.NO_BOOKS_AVAILABLE_MESSAGE);
        }

        BorrowPaperBookHistory borrowPaperBookHistory = new BorrowPaperBookHistory();
        borrowPaperBookHistory.setPaperBook(paperBook);
        borrowPaperBookHistory.setUser(loggedUser);
        borrowPaperBookHistory.setBorrowDateTime(LocalDateTime.now());
        borrowPaperBookHistory.setReturnDateTime(LocalDateTime.now().plusDays(AppConstants.DEFAULT_DAYS_TO_RETURN_A_BOOK));
        borrowPaperBookHistory.setReturned(false);

        paperBookService.updateNumberOfBooksAfterBorrowing(bookId);
        return mapToDTO(borrowPaperBookHistoryRepository.save(borrowPaperBookHistory));
    }

    @Override
    public BorrowPaperBookHistoryDTO returnPaperBookByHistoryId(Long borrowPaperBookHistoryId) {
        BorrowPaperBookHistory borrowPaperBookHistory = borrowPaperBookHistoryRepository.findById(borrowPaperBookHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow History", "id", borrowPaperBookHistoryId));

        User loggedUser = getLoggedUser();
        if (!Objects.equals(borrowPaperBookHistory.getUser().getId(), loggedUser.getId()))
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AppConstants.NO_VALID_BOOK_USER_MESSAGE);

        if (borrowPaperBookHistory.isReturned()) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AppConstants.BOOK_ALREADY_RETURNED_MESSAGE);
        }

        borrowPaperBookHistory.setReturned(true);
        paperBookService.updateNumberOfBooksAfterReturning(borrowPaperBookHistory.getPaperBook().getId());
        return mapToDTO(borrowPaperBookHistoryRepository.save(borrowPaperBookHistory));
    }

    @Override
    public BorrowPaperBookHistoryDTO postponeReturnDateByHistoryId(Long borrowPaperBookHistoryId, Long days) {
        BorrowPaperBookHistory borrowPaperBookHistory = borrowPaperBookHistoryRepository.findById(borrowPaperBookHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow History", "id", borrowPaperBookHistoryId));

        User loggedUser = getLoggedUser();
        if (!Objects.equals(borrowPaperBookHistory.getUser().getId(), loggedUser.getId())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AppConstants.NO_VALID_BOOK_USER_MESSAGE);
        }

        if (borrowPaperBookHistory.isReturned()) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AppConstants.BOOK_ALREADY_RETURNED_MESSAGE);
        }

        if (days < 1) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AppConstants.INVALID_POSTPONE_DAYS_MESSAGE);
        }

        if (ChronoUnit.DAYS.between(borrowPaperBookHistory.getBorrowDateTime(),
                borrowPaperBookHistory.getReturnDateTime().plusDays(days)) > AppConstants.DEFAULT_MAX_POSTPONE_DAYS) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AppConstants.LIMIT_POSTPONE_DAYS_MESSAGE);
        }

        borrowPaperBookHistory.setReturnDateTime(borrowPaperBookHistory.getReturnDateTime().plusDays(days));
        BorrowPaperBookHistory updatedBorrowPaperBookHistory = borrowPaperBookHistoryRepository.save(borrowPaperBookHistory);
        return mapToDTO(updatedBorrowPaperBookHistory);
    }

    private User getLoggedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.getUserByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));
    }

    private BorrowPaperBookHistoryDTO mapToDTO(BorrowPaperBookHistory borrowPaperBookHistory) {
        return mapper.map(borrowPaperBookHistory, BorrowPaperBookHistoryDTO.class);
    }
}
