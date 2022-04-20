package ch.cryptric.tex.cards.texcardsrestservice.api.response;

public class APIFlashcardsSets {

    private long[] ids;
    private String[] names;

    public APIFlashcardsSets() {

    }

    public APIFlashcardsSets(long[] ids, String[] names) {
        this.ids = ids;
        this.names = names;
    }

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

}
