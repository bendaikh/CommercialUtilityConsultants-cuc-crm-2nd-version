package mycrm.services;

import mycrm.models.NoteTransferHistory;

import java.util.List;

public interface NoteTransferHistoryService {
    void save(NoteTransferHistory noteTransferHistory);

    List<NoteTransferHistory> findTaggedNoteHistoryByNoteIdAndType(long noteId, String noteType);
}
