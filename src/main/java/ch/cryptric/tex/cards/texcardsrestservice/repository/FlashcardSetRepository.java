package ch.cryptric.tex.cards.texcardsrestservice.repository;

import ch.cryptric.tex.cards.texcardsrestservice.model.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Long> {

    boolean existsByIDAndCreatorID(long ID, long creatorID);

    @Transactional
    long deleteByID(long ID);

}
