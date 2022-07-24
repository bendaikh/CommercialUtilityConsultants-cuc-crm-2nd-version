package mycrm.services;

import mycrm.models.User;
import mycrm.models.UserNoteTransferUpdate;
import mycrm.models.UserTaggedNotesPackage;

public interface UserTransferService {
    UserTaggedNotesPackage findUserTaggedNotes(User user);

    boolean transferUserNotes(UserNoteTransferUpdate userNoteTransferUpdate);
}
