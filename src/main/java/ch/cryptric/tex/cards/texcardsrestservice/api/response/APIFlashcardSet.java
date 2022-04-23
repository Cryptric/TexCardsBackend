package ch.cryptric.tex.cards.texcardsrestservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIFlashcardSet {

    private long id;
    private String flashcardSetName;

    private String[] terms;
    private String[] definitions;
    private Set<Integer> stars;
    private boolean owner;
    private boolean editPermission;


}
