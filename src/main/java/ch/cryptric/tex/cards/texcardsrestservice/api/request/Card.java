package ch.cryptric.tex.cards.texcardsrestservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Card {

    private String term;
    private String definition;
    private int alignment;

}
