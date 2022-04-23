package ch.cryptric.tex.cards.texcardsrestservice.repository;

import ch.cryptric.tex.cards.texcardsrestservice.model.FlashcardStar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FlashcardStarRepository extends JpaRepository<FlashcardStar, Long> {

    List<FlashcardStar> findByUserIDAndFlashcardSetID(long userID, long flashcardSetID);

    @Transactional
    long deleteByUserIDAndFlashcardSetIDAndFlashcardID(long userID, long flashcardSetID, long flashcardID);

    @Transactional
    long deleteByFlashcardSetID(long flashcardSetID);

}
