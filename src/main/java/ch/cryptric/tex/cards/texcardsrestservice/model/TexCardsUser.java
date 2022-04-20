package ch.cryptric.tex.cards.texcardsrestservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tex_cards_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TexCardsUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String email;
    private String password;

}
