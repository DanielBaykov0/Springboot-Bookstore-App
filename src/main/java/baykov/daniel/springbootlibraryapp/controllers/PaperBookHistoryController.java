package baykov.daniel.springbootlibraryapp.controllers;

import baykov.daniel.springbootlibraryapp.payload.dto.UserBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.services.PaperBookHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class PaperBookHistoryController {

    private final PaperBookHistoryService paperBookHistoryService;

    public PaperBookHistoryController(PaperBookHistoryService paperBookHistoryService) {
        this.paperBookHistoryService = paperBookHistoryService;
    }

    @PostMapping("{userId}/borrow/{bookId}")
    public ResponseEntity<UserBookHistoryDTO> borrowPaperBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return new ResponseEntity<>(paperBookHistoryService.borrowPaperBookById(userId, bookId), HttpStatus.CREATED);
    }

    @PatchMapping("{userId}/return/{id}")
    public ResponseEntity<UserBookHistoryDTO> returnPaperBook(@PathVariable Long userId, @PathVariable(name = "id") Long borrowHistoryId) {
        return ResponseEntity.ok(paperBookHistoryService.returnPaperBookByHistoryId(userId, borrowHistoryId));
    }

    @PatchMapping("{userId}/postpone/{id}")
    public ResponseEntity<UserBookHistoryDTO> postponeBook(@PathVariable Long userId, @PathVariable(name = "id") Long borrowHistoryId, @RequestParam("days") Long days) {
        return ResponseEntity.ok(paperBookHistoryService.postponeReturnDateByHistoryId(userId, borrowHistoryId, days));
    }
}
