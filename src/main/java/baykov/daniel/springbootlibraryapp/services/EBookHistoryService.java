package baykov.daniel.springbootlibraryapp.services;

public interface EBookHistoryService {

    EBookHistoryDTO readEBook(Long userId, Long ebookId);

    EBookHistoryDTO downloadEBook(Long userId, Long ebookId);

    EBookHistoryDTO getUserAndEBook(Long userId, Long ebookId);

    EBookHistoryResponse getAllEBooksByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
}
