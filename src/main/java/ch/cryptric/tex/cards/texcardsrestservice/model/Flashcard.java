package ch.cryptric.tex.cards.texcardsrestservice.model;

import ch.cryptric.tex.cards.texcardsrestservice.api.request.Card;
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
    @Column(length = 1000)
    private String definition;
    @Column(length = 1000)
    private String term;
    private int alignment;

    public Flashcard(long SetID, String Definition, String Term, int alignment) {
        this.setID = SetID;
        this.definition = Definition;
        this.term = Term;
        this.alignment = alignment;
    }

    public boolean equals(Flashcard card) {
        return definition.equals(card.getDefinition()) && term.equals(card.getTerm()) && setID == card.getSetID() && alignment == card.getAlignment();
    }

    public boolean equals(Card card) {
        return nullStringEquals(definition, card.getDefinition()) && nullStringEquals(term, card.getTerm()) && alignment == card.getAlignment();
    }

    public static Flashcard findFirstEquals(List<Flashcard> flashcards, Card card) {
        return flashcards.stream().filter(x -> x.equals(card)).findFirst().orElseThrow();
    }

    private static boolean nullStringEquals(String s1, String s2) {
        return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
    }

}
