package baykov.daniel.springbootlibraryapp.controllers;

import baykov.daniel.springbootlibraryapp.payload.dto.BorrowPaperBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.services.BorrowPaperBookHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BorrowPaperBookHistoryController {

    private final BorrowPaperBookHistoryService borrowPaperBookHistoryService;

    public BorrowPaperBookHistoryController(BorrowPaperBookHistoryService borrowPaperBookHistoryService) {
        this.borrowPaperBookHistoryService = borrowPaperBookHistoryService;
    }

    @PostMapping("borrow/{id}")
    public ResponseEntity<BorrowPaperBookHistoryDTO> borrowPaperBook(@PathVariable(name = "id") Long bookId) {
        return new ResponseEntity<>(borrowPaperBookHistoryService.borrowPaperBookById(bookId), HttpStatus.CREATED);
    }

    @PatchMapping("return/{id}")
    public ResponseEntity<BorrowPaperBookHistoryDTO> returnPaperBook(@PathVariable(name = "id") Long borrowHistoryId) {
        return ResponseEntity.ok(borrowPaperBookHistoryService.returnPaperBookByHistoryId(borrowHistoryId));
    }

    @PatchMapping("postpone/{id}")
    public ResponseEntity<BorrowPaperBookHistoryDTO> postponeBook(@PathVariable(name = "id") Long borrowHistoryId, @RequestParam("days") Long days) {
        return ResponseEntity.ok(borrowPaperBookHistoryService.postponeReturnDateByHistoryId(borrowHistoryId, days));
    }
}
