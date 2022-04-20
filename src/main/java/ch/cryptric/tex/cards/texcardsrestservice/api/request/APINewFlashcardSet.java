package ch.cryptric.tex.cards.texcardsrestservice.api.request;

public class APINewFlashcardSet {

    private String name;

    public APINewFlashcardSet() {

    }

    public APINewFlashcardSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
