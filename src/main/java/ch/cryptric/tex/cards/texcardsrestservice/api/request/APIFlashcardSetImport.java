package ch.cryptric.tex.cards.texcardsrestservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIFlashcardSetImport {

    private long flashcardSetID;
    private String tdSeparator;
    private String cardSeparator;
    private int alignment;
    private String inputTxt;

}
