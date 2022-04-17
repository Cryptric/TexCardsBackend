package ch.cryptric.tex.cards.texcardsrestservice;

import java.util.Arrays;

public class CardSet {

    private String[] definitions;
    private String[] terms;
    private long id;

    public CardSet(String[] aDefinitions, String[] aTerms, long aId) {
        definitions = aDefinitions;
        terms = aTerms;
        id = aId;
    }

    public String[] getDefinitions() {
        return definitions;
    }

    public void setDefinitions(String[] aDefinitions) {
        this.definitions = aDefinitions;
    }

    public String[] getTerms() {
        return terms;
    }

    public void setTerms(String[] aTerms) {
        this.terms = aTerms;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CardSet{" +
                "definitions=" + Arrays.toString(definitions) +
                ", terms=" + Arrays.toString(terms) +
                ", id=" + id +
                '}';
    }
}
