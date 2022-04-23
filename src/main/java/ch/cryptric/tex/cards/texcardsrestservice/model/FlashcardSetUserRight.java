package ch.cryptric.tex.cards.texcardsrestservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flashcard_set_user_rights")
@Entity
public class FlashcardSetUserRight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private long flashcardSetID;
    private long userID;
    private boolean readPermission;
    private boolean writePermission;

    public FlashcardSetUserRight(long flashcardSetID, long userID, boolean readPermission, boolean writePermission) {
        this.flashcardSetID = flashcardSetID;
        this.userID = userID;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }

}
