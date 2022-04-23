package ch.cryptric.tex.cards.texcardsrestservice.service;

import ch.cryptric.tex.cards.texcardsrestservice.model.FlashcardSetUserRight;
import ch.cryptric.tex.cards.texcardsrestservice.repository.FlashcardSetUserRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlashcardSetUserRightService {

    @Autowired
    FlashcardSetUserRightRepository flashcardSetUserRightRepository;

    public FlashcardSetUserRight findByFlashcardSetIDAndUserID(long flashcardSetID, long userID) {
        try {
            return flashcardSetUserRightRepository.findByFlashcardSetIDAndUserID(flashcardSetID, userID);
        } catch (Exception e) {
            e.printStackTrace();
            return new FlashcardSetUserRight(-1, flashcardSetID, userID, false, false);
        }
    }

    public void save(FlashcardSetUserRight flashcardSetUserRight) {
        flashcardSetUserRightRepository.save(flashcardSetUserRight);
    }

    public List<FlashcardSetUserRight> findByUserIDAndReadPermission(long userID) {
        return flashcardSetUserRightRepository.findByUserIDAndReadPermission(userID, true);
    }

    public boolean hasUserReadPermission(long userId, long flashcardSetID) {
        return flashcardSetUserRightRepository.existsByUserIDAndFlashcardSetIDAndReadPermission(userId, flashcardSetID, true);
    }

    public boolean hasUserWritePermission(long userId, long flashcardSetID) {
        return flashcardSetUserRightRepository.existsByUserIDAndFlashcardSetIDAndWritePermission(userId, flashcardSetID, true);
    }

    public void deleteAllByFlashcardSetID(long flashcardSetID) {
        flashcardSetUserRightRepository.deleteByFlashcardSetID(flashcardSetID);
    }

    public void deleteByFlashcardSetIDAndUserID(long flashcardSetID, long userID) {
        flashcardSetUserRightRepository.deleteByFlashcardSetIDAndUserID(flashcardSetID, userID);
    }

    public List<FlashcardSetUserRight> findByFlashcardSetID(long flashcardSetID) {
        return flashcardSetUserRightRepository.findByFlashcardSetID(flashcardSetID);
    }

    public boolean existsUserPermission(long userID, long flashcardSetID) {
        return flashcardSetUserRightRepository.existsByUserIDAndFlashcardSetID(userID, flashcardSetID);
    }
}
