package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.payload.dto.UserBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.payload.response.UserBookHistoryResponse;
import baykov.daniel.springbootlibraryapp.service.UserBookHistoryService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserBookHistoryController {

    private final UserBookHistoryService userBookHistoryService;

    public UserBookHistoryController(UserBookHistoryService userBookHistoryService) {
        this.userBookHistoryService = userBookHistoryService;
    }

    @PostMapping("{userId}/read/{bookId}")
    public ResponseEntity<UserBookHistoryDTO> readBookById(@PathVariable Long userId,
                                                           @PathVariable Long bookId) {
        return new ResponseEntity<>(userBookHistoryService.readBookById(userId, bookId), HttpStatus.CREATED);
    }

    @PostMapping("{userId}/download/{bookId}")
    public ResponseEntity<UserBookHistoryDTO> downloadBookById(@PathVariable Long userId,
                                                               @PathVariable Long bookId) {
        return new ResponseEntity<>(userBookHistoryService.downloadBookById(userId, bookId), HttpStatus.CREATED);
    }

    @PostMapping("{userId}/borrow/{bookId}")
    public ResponseEntity<UserBookHistoryDTO> borrowBookById(@PathVariable Long userId,
                                                             @PathVariable Long bookId) {
        return new ResponseEntity<>(userBookHistoryService.borrowBookById(userId, bookId), HttpStatus.CREATED);
    }

    @PatchMapping("{userId}/return/{id}")
    public ResponseEntity<UserBookHistoryDTO> returnBookById(@PathVariable Long userId,
                                                             @PathVariable(name = "id") Long borrowHistoryId) {
        return ResponseEntity.ok(userBookHistoryService.returnBookByHistoryId(userId, borrowHistoryId));
    }

    @PatchMapping("{userId}/postpone/{id}")
    public ResponseEntity<UserBookHistoryDTO> postponeBookById(@PathVariable Long userId,
                                                               @PathVariable(name = "id") Long borrowHistoryId,
                                                               @RequestParam("days") Long days) {
        return ResponseEntity.ok(userBookHistoryService.postponeReturnDateByHistoryId(userId, borrowHistoryId, days));
    }

    @GetMapping("{userId}/books/{bookId}")
    public ResponseEntity<UserBookHistoryDTO> getBookByUserId(@PathVariable Long userId,
                                                              @PathVariable Long bookId) {
        return ResponseEntity.ok(userBookHistoryService.getUserAndBook(userId, bookId));
    }

    @GetMapping("{userId}/books")
    public ResponseEntity<UserBookHistoryResponse> getAllEBooksByUserId(
            @PathVariable Long userId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(userBookHistoryService.getAllBooksByUserId(userId, pageNo, pageSize, sortBy, sortDir));
    }
}
