package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.PaperBookHistoryDTO;

public interface PaperBookHistoryService {

    PaperBookHistoryDTO borrowPaperBookById(Long userId, Long bookId);

    PaperBookHistoryDTO returnPaperBookByHistoryId(Long userId, Long borrowPaperBookHistoryId);

    PaperBookHistoryDTO postponeReturnDateByHistoryId(Long userId, Long borrowPaperBookHistoryId, Long days);
}
