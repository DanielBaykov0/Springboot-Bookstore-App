package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.EBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.payload.response.EBookHistoryResponse;

public interface EBookHistoryService {

    EBookHistoryDTO readEBook(Long userId, Long ebookId);

    EBookHistoryDTO downloadEBook(Long userId, Long ebookId);

    EBookHistoryDTO getUserAndEBook(Long userId, Long ebookId);

    EBookHistoryResponse getAllEBooksByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
}
