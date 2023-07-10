package baykov.daniel.springbootlibraryapp.controllers;

import baykov.daniel.springbootlibraryapp.payload.dto.PaperBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.services.PaperBookHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BorrowPaperBookHistoryController {

    private final PaperBookHistoryService paperBookHistoryService;

    public BorrowPaperBookHistoryController(PaperBookHistoryService paperBookHistoryService) {
        this.paperBookHistoryService = paperBookHistoryService;
    }

    @PostMapping("borrow/{id}")
    public ResponseEntity<PaperBookHistoryDTO> borrowPaperBook(@PathVariable(name = "id") Long bookId) {
        return new ResponseEntity<>(paperBookHistoryService.borrowPaperBookById(bookId), HttpStatus.CREATED);
    }

    @PatchMapping("return/{id}")
    public ResponseEntity<PaperBookHistoryDTO> returnPaperBook(@PathVariable(name = "id") Long borrowHistoryId) {
        return ResponseEntity.ok(paperBookHistoryService.returnPaperBookByHistoryId(borrowHistoryId));
    }

    @PatchMapping("postpone/{id}")
    public ResponseEntity<PaperBookHistoryDTO> postponeBook(@PathVariable(name = "id") Long borrowHistoryId, @RequestParam("days") Long days) {
        return ResponseEntity.ok(paperBookHistoryService.postponeReturnDateByHistoryId(borrowHistoryId, days));
    }
}
