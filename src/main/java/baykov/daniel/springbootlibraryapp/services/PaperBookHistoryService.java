package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.PaperBookHistoryDTO;

public interface PaperBookHistoryService {

    PaperBookHistoryDTO borrowPaperBookById(Long bookId);

    PaperBookHistoryDTO returnPaperBookByHistoryId(Long borrowPaperBookHistoryId);

    PaperBookHistoryDTO postponeReturnDateByHistoryId(Long borrowPaperBookHistoryId, Long days);
}
