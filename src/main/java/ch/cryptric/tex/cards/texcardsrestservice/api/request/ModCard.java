package ch.cryptric.tex.cards.texcardsrestservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModCard {

    private Card oldCard;
    private Card newCard;
    private APIFlashcardSetEdit.EditType editType;


}
