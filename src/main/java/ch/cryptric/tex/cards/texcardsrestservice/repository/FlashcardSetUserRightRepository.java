package ch.cryptric.tex.cards.texcardsrestservice.repository;

import ch.cryptric.tex.cards.texcardsrestservice.model.FlashcardSetUserRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FlashcardSetUserRightRepository extends JpaRepository<FlashcardSetUserRight, Long> {


    FlashcardSetUserRight findByFlashcardSetIDAndUserID(long flashcardSetID, long userID);

    List<FlashcardSetUserRight> findByUserIDAndReadPermission(long userID, boolean readPermission);

    boolean existsByUserIDAndFlashcardSetIDAndReadPermission(long userID, long flashcardSetID, boolean readPermission);

    boolean existsByUserIDAndFlashcardSetIDAndWritePermission(long userID, long flashcardSetID, boolean writePermission);

    boolean existsByUserIDAndFlashcardSetID(long userID, long flashcardSetID);

    List<FlashcardSetUserRight> findByFlashcardSetID(long flashcardSetID);

    @Transactional
    long deleteByFlashcardSetID(long flashcardSetID);

    @Transactional
    long deleteByFlashcardSetIDAndUserID(long flashcardSetID, long userID);


}
