package ch.cryptric.tex.cards.texcardsrestservice.api.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class APIFlashcardSetEdit {

    public enum EditType {Modify, Delete, Add}

    private long id;
    private String flashcardSetName;
    private ModCard[] editMap;

}
