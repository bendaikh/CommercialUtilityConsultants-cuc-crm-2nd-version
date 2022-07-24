package mycrm.repositories;

import mycrm.models.NoteTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteTransferHistoryRepository extends JpaRepository<NoteTransferHistory, Long> {
    @Query("SELECT t FROM NoteTransferHistory t WHERE t.noteId=(:noteId) AND t.noteType=(:noteType) ORDER BY t.dateCreated DESC")
    List<NoteTransferHistory> findByTaggedNoteIdAndNoteType(@Param("noteId") long noteId, @Param("noteType") String noteType);
}
