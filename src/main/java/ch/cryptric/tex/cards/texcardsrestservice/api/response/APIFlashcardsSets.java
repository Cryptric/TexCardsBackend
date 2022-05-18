package ch.cryptric.tex.cards.texcardsrestservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class APIFlashcardsSets {

    private long[] ids;
    private String[] names;
    private boolean[] writePermission;
    private boolean[] owner;

}
