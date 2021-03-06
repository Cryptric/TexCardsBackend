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
@Table(name = "flashcard_sets")
@Entity
public class FlashcardSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String name;
    private long creatorID;

    public FlashcardSet(String name, long creatorID) {
        this.name = name;
        this.creatorID = creatorID;
    }

}
