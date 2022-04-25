package ch.cryptric.tex.cards.texcardsrestservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    /**
     * compare two flashcards
     * @param card the flashcard to compare against
     * @return true if either the definition or the term is equals or both
     */
    public boolean similar(Flashcard card) {
        return setID == card.getSetID() && (definition.equals(card.getDefinition()) || term.equals(card.getTerm()));
    }

    public boolean similar(String definition, String term) {
        return this.definition.equals(definition) || this.term.equals(term);
    }

    public boolean equals(Flashcard card) {
        return definition.equals(card.getDefinition()) && term.equals(card.getTerm()) && setID == card.getSetID();
    }

    public static int similarToOne(List<Flashcard> cards, String definition, String term) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).similar(definition, term)) {
                return i;
            }
        }
        return -1;
    }
}
