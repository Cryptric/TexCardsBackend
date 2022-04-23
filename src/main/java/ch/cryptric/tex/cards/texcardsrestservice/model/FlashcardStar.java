package ch.cryptric.tex.cards.texcardsrestservice.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flashcard_stars")
@Entity
public class FlashcardStar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private long flashcardID;
    private long flashcardSetID;
    private long userID;

    public FlashcardStar(long userID, long flashcardSetID, long flashcardID) {
        this.flashcardID = flashcardID;
        this.flashcardSetID = flashcardSetID;
        this.userID = userID;
    }

}
