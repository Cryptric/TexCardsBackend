package ch.cryptric.tex.cards.texcardsrestservice.api.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class APIRegister {

    private String username;
    private String email;
    private String password;

}
