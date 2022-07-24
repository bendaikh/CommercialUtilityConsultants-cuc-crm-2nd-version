package mycrm.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserTaggedNotesPackage {
    private int numberOfTaggedNotes;

    private int numberOfTaggedChildNotes;
}
