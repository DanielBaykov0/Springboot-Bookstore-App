package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.payload.dto.UserBookHistoryDTO;
import baykov.daniel.springbootlibraryapp.payload.response.UserBookHistoryResponse;

public interface UserBookHistoryService {

    UserBookHistoryDTO readBookById(Long userId, Long bookId);

    UserBookHistoryDTO downloadBookById(Long userId, Long bookId);

    UserBookHistoryDTO getUserAndBook(Long userId, Long bookId);

    UserBookHistoryResponse getAllBooksByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);

    UserBookHistoryDTO borrowBookById(Long userId, Long bookId);

    UserBookHistoryDTO returnBookByHistoryId(Long userId, Long userBookHistoryId);

    UserBookHistoryDTO postponeReturnDateByHistoryId(Long userId, Long userBookHistoryId, Long days);
}
