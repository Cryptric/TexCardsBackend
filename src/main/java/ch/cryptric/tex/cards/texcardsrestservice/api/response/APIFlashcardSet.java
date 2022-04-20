package ch.cryptric.tex.cards.texcardsrestservice.api.response;

import java.util.Arrays;

public class APIFlashcardSet {

    private long id;
    private String flashcardSetName;

    private String[] terms;
    private String[] definitions;

    public APIFlashcardSet() {

    }

    public APIFlashcardSet(long id, String flashcardSetName, String[] terms, String[] definitions) {
        this.id = id;
        this.flashcardSetName = flashcardSetName;
        this.terms = terms;
        this.definitions = definitions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlashcardSetName() {
        return flashcardSetName;
    }

    public void setFlashcardSetName(String flashcardSetName) {
        this.flashcardSetName = flashcardSetName;
    }

    public String[] getTerms() {
        return terms;
    }

    public void setTerms(String[] terms) {
        this.terms = terms;
    }

    public String[] getDefinitions() {
        return definitions;
    }

    public void setDefinitions(String[] definitions) {
        this.definitions = definitions;
    }

    @Override
    public String toString() {
        return "APIFlashcardSet{" +
                "id=" + id +
                ", flashcardSetName='" + flashcardSetName + '\'' +
                ", terms=" + Arrays.toString(terms) +
                ", definitions=" + Arrays.toString(definitions) +
                '}';
    }
}
