package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.PaperBookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.PaperBookResponse;

public interface PaperBookService {

    PaperBookDTO createPaperBook(PaperBookDTO paperBookDTO);

    PaperBookDTO getPaperBookById(long id);

    PaperBookResponse getAllPaperBooks(int pageNo, int pageSize, String sortBy, String sortDir);

    PaperBookDTO updatePaperBook(PaperBookDTO paperBookDTO, long id);

    void deletePaperBook(long id);
}
