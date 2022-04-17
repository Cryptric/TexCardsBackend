package ch.cryptric.tex.cards.texcardsrestservice;

import javax.persistence.*;

@Entity
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private long setID;
    private String definition;
    private String term;

    public Flashcard() {

    }

    public Flashcard(long SetID, String Definition, String Term) {
        this.ID = ID;
        this.setID = SetID;
        this.definition = Definition;
        this.term = Term;
    }

    public Flashcard(long ID, long SetID, String Definition, String Term) {
        this.ID = ID;
        this.setID = SetID;
        this.definition = Definition;
        this.term = Term;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getSetID() {
        return setID;
    }

    public void setSetID(long SetID) {
        this.setID = SetID;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String Definition) {
        this.definition = Definition;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String Term) {
        this.term = Term;
    }

}
