package baykov.daniel.springbootlibraryapp.controllers;

import baykov.daniel.springbootlibraryapp.payload.dto.EBookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.EBookResponse;
import baykov.daniel.springbootlibraryapp.services.EBookService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import baykov.daniel.springbootlibraryapp.utils.Messages;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ebooks")
public class EBookController {

    private final EBookService eBookService;

    public EBookController(EBookService eBookService) {
        this.eBookService = eBookService;
    }

    @PostMapping
    public ResponseEntity<EBookDTO> createEBook(@Valid @RequestBody EBookDTO eBookDTO) {
        return new ResponseEntity<>(eBookService.createEBook(eBookDTO), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<EBookDTO> getEBookById(@PathVariable(name = "id") Long ebookId) {
        return ResponseEntity.ok(eBookService.getEBookById(ebookId));
    }

    @GetMapping
    public ResponseEntity<EBookResponse> getAllEBooks(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok(eBookService.getAllEBooks(pageNo, pageSize, sortBy, sortDir));
    }

    @PutMapping("{id}")
    public ResponseEntity<EBookDTO> updateEBookById(@PathVariable(name = "id") Long ebookId,
                                                    @Valid @RequestBody EBookDTO eBookDTO) {
        return ResponseEntity.ok(eBookService.updateEBook(eBookDTO, ebookId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEBookById(@PathVariable(name = "id") Long ebookId) {
        eBookService.deleteEBook(ebookId);
        return ResponseEntity.ok(Messages.EBOOK_DELETE_MESSAGE);
    }
}
