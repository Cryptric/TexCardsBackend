package ch.cryptric.tex.cards.texcardsrestservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIUserPermission {

    private String username;
    private long flashcardSetID;
    private boolean readPermission;
    private boolean writePermission;

}
