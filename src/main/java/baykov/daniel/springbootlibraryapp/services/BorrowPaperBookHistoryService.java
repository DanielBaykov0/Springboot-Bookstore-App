package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.BorrowPaperBookHistoryDTO;

public interface BorrowPaperBookHistoryService {

    BorrowPaperBookHistoryDTO borrowPaperBookById(Long bookId);

    BorrowPaperBookHistoryDTO returnPaperBookByHistoryId(Long borrowPaperBookHistoryId);

    BorrowPaperBookHistoryDTO postponeReturnDateByHistoryId(Long borrowPaperBookHistoryId, Long days);
}
