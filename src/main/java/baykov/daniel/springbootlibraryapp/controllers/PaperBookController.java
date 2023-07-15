package baykov.daniel.springbootlibraryapp.controllers;

import baykov.daniel.springbootlibraryapp.payload.dto.BookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.BookResponse;
import baykov.daniel.springbootlibraryapp.services.PaperBookService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import baykov.daniel.springbootlibraryapp.utils.Messages;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/books")
public class PaperBookController {

    private final PaperBookService paperBookService;

    public PaperBookController(PaperBookService paperBookService) {
        this.paperBookService = paperBookService;
    }

    @PostMapping
    public ResponseEntity<BookDTO> createPaperBook(@Valid @RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(paperBookService.createPaperBook(bookDTO), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<BookDTO> getPaperBookById(@PathVariable(name = "id") Long paperBookId) {
        return ResponseEntity.ok(paperBookService.getPaperBookById(paperBookId));
    }

    @GetMapping
    public ResponseEntity<BookResponse> getAllPaperBooks(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(paperBookService.getAllPaperBooks(pageNo, pageSize, sortBy, sortDir));
    }

    @PutMapping("{id}")
    public ResponseEntity<BookDTO> updatePaperBookById(@PathVariable(name = "id") Long paperBookId,
                                                       @Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(paperBookService.updatePaperBook(bookDTO, paperBookId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePaperBookById(@PathVariable(name = "id") Long paperBookId) {
        paperBookService.deletePaperBook(paperBookId);
        return ResponseEntity.ok(Messages.PAPER_BOOK_DELETE_MESSAGE);
    }
}
