package ch.cryptric.tex.cards.texcardsrestservice.repository;

import ch.cryptric.tex.cards.texcardsrestservice.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findBysetID(long setID);

    @Transactional
    void deleteBysetID(long setID);

}
