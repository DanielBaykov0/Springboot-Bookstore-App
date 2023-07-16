package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.payload.dto.UserBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.payload.response.UserBookHistoryResponse;
import baykov.daniel.springbootlibraryapp.service.UserBookHistoryService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "CRUD REST APIs for User Cloud Book History Resource")
public class UserBookHistoryController {

    private final UserBookHistoryService userBookHistoryService;

    public UserBookHistoryController(UserBookHistoryService userBookHistoryService) {
        this.userBookHistoryService = userBookHistoryService;
    }

    @Operation(
            summary = "Read A Book REST API",
            description = "Read A Book REST API is used to read a book from database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("{userId}/read/{bookId}")
    public ResponseEntity<UserBookHistoryDTO> readBookById(@PathVariable Long userId,
                                                           @PathVariable Long bookId) {
        return new ResponseEntity<>(userBookHistoryService.readBookById(userId, bookId), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Download A Book REST API",
            description = "Download A Book REST API is used to download a book from database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("{userId}/download/{bookId}")
    public ResponseEntity<UserBookHistoryDTO> downloadBookById(@PathVariable Long userId,
                                                               @PathVariable Long bookId) {
        return new ResponseEntity<>(userBookHistoryService.downloadBookById(userId, bookId), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Borrow Book REST API",
            description = "Borrow Book REST API is used to borrow a particular book from database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("{userId}/borrow/{bookId}")
    public ResponseEntity<UserBookHistoryDTO> borrowBookById(@PathVariable Long userId,
                                                             @PathVariable Long bookId) {
        return new ResponseEntity<>(userBookHistoryService.borrowBookById(userId, bookId), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Return Borrowed Book REST API",
            description = "Return Borrowed Book REST API is used to return a particular borrowed book in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PatchMapping("{userId}/return/{id}")
    public ResponseEntity<UserBookHistoryDTO> returnBookById(@PathVariable Long userId,
                                                             @PathVariable(name = "id") Long borrowHistoryId) {
        return ResponseEntity.ok(userBookHistoryService.returnBookByHistoryId(userId, borrowHistoryId));
    }

    @Operation(
            summary = "Postpone Return Date Of Borrowed Book By Days REST API",
            description = "Postpone Return Date Of Borrowed Book By Days REST API is used to postpone the days of a borrowed book in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PatchMapping("{userId}/postpone/{id}")
    public ResponseEntity<UserBookHistoryDTO> postponeBookById(@PathVariable Long userId,
                                                               @PathVariable(name = "id") Long borrowHistoryId,
                                                               @RequestParam("days") Long days) {
        return ResponseEntity.ok(userBookHistoryService.postponeReturnDateByHistoryId(userId, borrowHistoryId, days));
    }

    @Operation(
            summary = "Get User's Book By Book Id REST API",
            description = "Search User's Book By Book Id REST API is used to search a user's book by book id from database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("{userId}/books/{bookId}")
    public ResponseEntity<UserBookHistoryDTO> getBookByUserId(@PathVariable Long userId,
                                                              @PathVariable Long bookId) {
        return ResponseEntity.ok(userBookHistoryService.getUserAndBook(userId, bookId));
    }

    @Operation(
            summary = "Get All User's Books REST API",
            description = "Search All User's Books REST API is used to get all books by a user's id from database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
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
