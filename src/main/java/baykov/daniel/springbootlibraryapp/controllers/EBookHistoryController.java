package baykov.daniel.springbootlibraryapp.controllers;

import baykov.daniel.springbootlibraryapp.payload.dto.EBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.payload.response.EBookHistoryResponse;
import baykov.daniel.springbootlibraryapp.services.EBookHistoryService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class EBookHistoryController {

    private final EBookHistoryService eBookHistoryService;

    public EBookHistoryController(EBookHistoryService eBookHistoryService) {
        this.eBookHistoryService = eBookHistoryService;
    }

    @PostMapping("{userId}/read/{ebookId}")
    public ResponseEntity<EBookHistoryDTO> readEBook(@PathVariable Long userId, @PathVariable Long ebookId) {
        return new ResponseEntity<>(eBookHistoryService.readEBook(userId, ebookId), HttpStatus.CREATED);
    }

    @PostMapping("{userId}/download/{ebookId}")
    public ResponseEntity<EBookHistoryDTO> downloadEBook(@PathVariable Long userId, @PathVariable Long ebookId) {
        return new ResponseEntity<>(eBookHistoryService.downloadEBook(userId, ebookId), HttpStatus.CREATED);
    }

    @GetMapping("{userId}/ebooks/{ebookId}")
    public ResponseEntity<EBookHistoryDTO> getEBookByUser(@PathVariable Long userId, @PathVariable Long ebookId) {
        return ResponseEntity.ok(eBookHistoryService.getUserAndEBook(userId, ebookId));
    }

    @GetMapping("{userId}/ebooks")
    public ResponseEntity<EBookHistoryResponse> getAllEBooksByUser(
            @PathVariable Long userId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(eBookHistoryService.getAllEBooksByUserId(userId, pageNo, pageSize, sortBy, sortDir));
    }
}
