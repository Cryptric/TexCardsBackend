package ch.cryptric.tex.cards.texcardsrestservice.repository;

import ch.cryptric.tex.cards.texcardsrestservice.model.TexCardsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TexCardsUserRepository extends JpaRepository<TexCardsUser, Long> {

    TexCardsUser findTexCardsUserByUserName(String username);

}
