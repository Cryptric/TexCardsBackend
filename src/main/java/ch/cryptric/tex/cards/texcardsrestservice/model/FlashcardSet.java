package ch.cryptric.tex.cards.texcardsrestservice.model;

import javax.persistence.*;

@Entity
public class FlashcardSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String Name;

    public FlashcardSet() {

    }

    public FlashcardSet(long ID, String Name) {
        this.ID = ID;
        this.Name = Name;
    }

    public FlashcardSet(String Name) {
        this.ID = ID;
        this.Name = Name;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}
