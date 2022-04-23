package ch.cryptric.tex.cards.texcardsrestservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class APIFlashcardStar {

    long flashcardSetID;
    String flashcardTerm;
    String flashcardDefinition;

}
