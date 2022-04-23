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
@Table(name = "flashcards")
@Entity
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private long setID;
    private String definition;
    private String term;

    public Flashcard(long SetID, String Definition, String Term) {
        this.ID = ID;
        this.setID = SetID;
        this.definition = Definition;
        this.term = Term;
    }

}
