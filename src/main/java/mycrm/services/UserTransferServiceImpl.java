package mycrm.services;

import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.NoteTransferHistory;
import mycrm.models.User;
import mycrm.models.UserNoteTransferUpdate;
import mycrm.models.UserTaggedNotesPackage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class UserTransferServiceImpl implements UserTransferService {

    private static Logger logger = LogManager.getLogger();

    private final CustomerNoteService customerNoteService;
    private final CustomerChildNoteService customerChildNoteService;
    private final NoteTransferHistoryService noteTransferHistoryService;

    @Autowired
    public UserTransferServiceImpl(CustomerNoteService customerNoteService,
                                   CustomerChildNoteService customerChildNoteService,
                                   NoteTransferHistoryService noteTransferHistoryService) {
        this.customerNoteService = customerNoteService;
        this.customerChildNoteService = customerChildNoteService;
        this.noteTransferHistoryService = noteTransferHistoryService;
    }

    @Override
    public UserTaggedNotesPackage findUserTaggedNotes(User user) {
        int taggedNotesByUser = customerNoteService.countTaggedNotesByUser(user);
        int taggedChildNotesByUser = customerChildNoteService.countTaggedChildNotesByUser(user);

        return UserTaggedNotesPackage
                .builder()
                .numberOfTaggedNotes(taggedNotesByUser)
                .numberOfTaggedChildNotes(taggedChildNotesByUser)
                .build();
    }

    @Override
    public boolean transferUserNotes(UserNoteTransferUpdate userNoteTransferUpdate) {
        boolean successful = false;
        User user = userNoteTransferUpdate.getNewUser();
        User previousUser = userNoteTransferUpdate.getPreviousUser();

        try {
            getTaggedNotes(previousUser).forEach(customerNote -> {
                customerNote.setTaggedUser(user);
                // save note
                customerNoteService.save(customerNote);

                // save transfer result
                noteTransferHistoryService.save(NoteTransferHistory
                        .builder()
                        .noteId(customerNote.getId())
                        .noteType("NOTE")
                        .user(user.getId())
                        .previousUser(previousUser.getId())
                        .build());
            });

            getTaggedChildNotes(previousUser).forEach(customerChildNote -> {
                customerChildNote.setReplyTaggedUser(user);
                // save child note
                customerChildNoteService.save(customerChildNote);

                // save transfer result
                noteTransferHistoryService.save(NoteTransferHistory
                        .builder()
                        .noteId(customerChildNote.getId())
                        .noteType("CHILD_NOTE")
                        .user(user.getId())
                        .previousUser(previousUser.getId())
                        .build());
            });
            successful = true;
        } catch (Exception e) {
            logger.info("Failed to transfer tagged user notes");
        }

        return successful;
    }

    private List<CustomerNote> getTaggedNotes(User user) {
        return customerNoteService.findByTaggedUser(user);
    }

    private List<CustomerChildNote> getTaggedChildNotes(User user) {
        return customerChildNoteService.findByTaggedUser(user);
    }
}
