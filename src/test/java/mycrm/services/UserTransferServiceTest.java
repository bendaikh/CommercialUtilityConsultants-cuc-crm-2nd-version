package mycrm.services;

import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.NoteTransferHistory;
import mycrm.models.User;
import mycrm.models.UserNoteTransferUpdate;
import mycrm.models.UserTaggedNotesPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTransferServiceTest {

    @InjectMocks
    private UserTransferServiceImpl service;

    @Mock
    private CustomerNoteService customerNoteService;

    @Mock
    private CustomerChildNoteService customerChildNoteService;

    @Mock
    private NoteTransferHistoryService noteTransferHistoryService;

    @Test
    public void shouldFindUserTaggedNotes() {
        User user = new User();
        user.setId(29L);
        user.setFirstName("FF");
        user.setLastName("RR");

        when(customerNoteService.countTaggedNotesByUser(any(User.class))).thenReturn(2);
        when(customerChildNoteService.countTaggedChildNotesByUser(any(User.class))).thenReturn(1);

        UserTaggedNotesPackage userTaggedNotes = service.findUserTaggedNotes(user);

        assertEquals(2, userTaggedNotes.getNumberOfTaggedNotes());
        assertEquals(1, userTaggedNotes.getNumberOfTaggedChildNotes());
    }

    @Test
    public void shouldSuccessfullyTransferUserTaggedNotes() {
        User newUser = new User();
        newUser.setId(1L);

        User previousUser = new User();
        previousUser.setId(1L);

        UserNoteTransferUpdate userNoteTransferUpdate = new UserNoteTransferUpdate();
        userNoteTransferUpdate.setNewUser(newUser);
        userNoteTransferUpdate.setPreviousUser(previousUser);

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(1L);
        customerNote.setTaggedUser(previousUser);


        List<CustomerNote> notesList = new ArrayList<>();
        notesList.add(customerNote);

        when(customerNoteService.findByTaggedUser(any(User.class))).thenReturn(notesList);

        CustomerChildNote customerChildNote = new CustomerChildNote();
        customerChildNote.setId(1L);
        customerChildNote.setReplyTaggedUser(previousUser);

        List<CustomerChildNote> notesChildList = new ArrayList<>();
        notesChildList.add(customerChildNote);

        when(customerChildNoteService.findByTaggedUser(any(User.class))).thenReturn(notesChildList);

        boolean successful = service.transferUserNotes(userNoteTransferUpdate);

        verify(noteTransferHistoryService, times(2)).save(any(NoteTransferHistory.class));

        assertTrue(successful);
    }

    @Test
    public void shouldNotTransferUserTaggedNotes() {
        User newUser = new User();
        newUser.setId(1L);

        User previousUser = new User();
        previousUser.setId(1L);

        UserNoteTransferUpdate userNoteTransferUpdate = new UserNoteTransferUpdate();
        userNoteTransferUpdate.setNewUser(newUser);
        userNoteTransferUpdate.setPreviousUser(previousUser);

        when(customerNoteService.findByTaggedUser(any(User.class))).thenThrow(Exception.class);

        boolean successful = service.transferUserNotes(userNoteTransferUpdate);

        verify(noteTransferHistoryService, times(0)).save(any(NoteTransferHistory.class));

        assertFalse(successful);
    }

}
