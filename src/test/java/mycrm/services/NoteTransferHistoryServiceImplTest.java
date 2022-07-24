package mycrm.services;

import mycrm.models.NoteTransferHistory;
import mycrm.repositories.NoteTransferHistoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoteTransferHistoryServiceImplTest {

    @InjectMocks
    private NoteTransferHistoryServiceImpl service;

    @Mock
    private NoteTransferHistoryRepository mockNoteTransferHistoryRepository;

    @Test
    public void shouldSaveNoteTransferHistory() {
        NoteTransferHistory history =
                NoteTransferHistory
                        .builder()
                        .previousUser(2L)
                        .noteType("CHILD_NOTE")
                        .noteId(1L)
                        .user(4L)
                        .build();

        service.save(history);

        verify(mockNoteTransferHistoryRepository, times(1)).save(any(NoteTransferHistory.class));
    }

    @Test
    public void shouldFindNoteTransferHistory() {

        List<NoteTransferHistory> list = new ArrayList<>();

        when(mockNoteTransferHistoryRepository.findByTaggedNoteIdAndNoteType(any(Long.class), any(String.class))).thenReturn(list);

        List<NoteTransferHistory> listOfNotes = service.findTaggedNoteHistoryByNoteIdAndType(1, "NOTE");

        assertEquals(0, listOfNotes.size());
    }

}
