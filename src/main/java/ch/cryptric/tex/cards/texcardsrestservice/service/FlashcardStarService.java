package ch.cryptric.tex.cards.texcardsrestservice.service;

import ch.cryptric.tex.cards.texcardsrestservice.model.FlashcardStar;
import ch.cryptric.tex.cards.texcardsrestservice.repository.FlashcardStarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlashcardStarService {

    @Autowired
    FlashcardStarRepository flashcardStarRepository;

    public void deleteAllByFlashcardSetID(long flashcardSetID) {
        flashcardStarRepository.deleteByFlashcardSetID(flashcardSetID);
    }

    public void addStar(FlashcardStar flashcardStar) {
        flashcardStarRepository.save(flashcardStar);
    }

    public List<FlashcardStar> findByUserIDAndFlashcardSetID(long userID, long flashcardSetID) {
        return flashcardStarRepository.findByUserIDAndFlashcardSetID(userID, flashcardSetID);
    }

    public void removeStarByUserIDAndFlashcardSetIDAndFlashcardID(long userID, long flashcardSetID, long flashcardID) {
        flashcardStarRepository.deleteByUserIDAndFlashcardSetIDAndFlashcardID(userID, flashcardSetID, flashcardID);
    }


}
