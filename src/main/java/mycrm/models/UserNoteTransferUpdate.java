package mycrm.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserNoteTransferUpdate {
    private User previousUser;

    private User newUser;
}
