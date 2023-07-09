package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.EBookDTO;
import baykov.daniel.springbootlibraryapp.payload.response.EBookResponse;

public interface EBookService {

    EBookDTO createEBook(EBookDTO eBookDTO);

    EBookDTO getEBookById(long id);

    EBookResponse getAllEBooks(int pageNo, int pageSize, String sortBy, String sortDir);

    EBookDTO updateEBook(EBookDTO eBookDTO, long id);

    void deleteEBook(long id);
}
