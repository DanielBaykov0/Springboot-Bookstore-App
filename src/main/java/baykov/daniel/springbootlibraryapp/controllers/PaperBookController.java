package baykov.daniel.springbootlibraryapp.controllers;

import baykov.daniel.springbootlibraryapp.payload.dto.PaperBookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.PaperBookResponse;
import baykov.daniel.springbootlibraryapp.services.PaperBookService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
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
    public ResponseEntity<PaperBookDTO> createPaperBook(@Valid @RequestBody PaperBookDTO paperBookDTO) {
        return new ResponseEntity<>(paperBookService.createPaperBook(paperBookDTO), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<PaperBookDTO> getPaperBookById(@PathVariable(name = "id") Long paperBookId) {
        return ResponseEntity.ok(paperBookService.getPaperBookById(paperBookId));
    }

    @GetMapping
    public ResponseEntity<PaperBookResponse> getAllPaperBooks(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(paperBookService.getAllPaperBooks(pageNo, pageSize, sortBy, sortDir));
    }

    @PutMapping("{id}")
    public ResponseEntity<PaperBookDTO> updatePaperBookById(@PathVariable(name = "id") Long paperBookId,
                                                            @Valid @RequestBody PaperBookDTO paperBookDTO) {
        return ResponseEntity.ok(paperBookService.updatePaperBook(paperBookDTO, paperBookId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePaperBookById(@PathVariable(name = "id") Long paperBookId) {
        paperBookService.deletePaperBook(paperBookId);
        return ResponseEntity.ok(AppConstants.PAPER_BOOK_DELETE_MESSAGE);
    }
}
