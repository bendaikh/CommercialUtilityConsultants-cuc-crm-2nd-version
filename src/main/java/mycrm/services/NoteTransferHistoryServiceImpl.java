package mycrm.services;

import mycrm.models.NoteTransferHistory;
import mycrm.repositories.NoteTransferHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class NoteTransferHistoryServiceImpl implements NoteTransferHistoryService {
    private final NoteTransferHistoryRepository noteTransferHistoryRepository;

    @Autowired
    public NoteTransferHistoryServiceImpl(NoteTransferHistoryRepository noteTransferHistoryRepository) {
        this.noteTransferHistoryRepository = noteTransferHistoryRepository;
    }

    @Override
    public void save(NoteTransferHistory noteTransferHistory) {
        this.noteTransferHistoryRepository.save(noteTransferHistory);
    }

    @Override
    public List<NoteTransferHistory> findTaggedNoteHistoryByNoteIdAndType(long noteId, String noteType) {
        return this.noteTransferHistoryRepository.findByTaggedNoteIdAndNoteType(noteId, noteType);
    }
}
