package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.UserBookHistoryDTO;

public interface PaperBookHistoryService {

    UserBookHistoryDTO borrowPaperBookById(Long userId, Long bookId);

    UserBookHistoryDTO returnPaperBookByHistoryId(Long userId, Long borrowPaperBookHistoryId);

    UserBookHistoryDTO postponeReturnDateByHistoryId(Long userId, Long borrowPaperBookHistoryId, Long days);
}
