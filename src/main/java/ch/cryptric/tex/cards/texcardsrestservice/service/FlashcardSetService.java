package ch.cryptric.tex.cards.texcardsrestservice.service;

import ch.cryptric.tex.cards.texcardsrestservice.model.FlashcardSet;
import ch.cryptric.tex.cards.texcardsrestservice.repository.FlashcardSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlashcardSetService {

    @Autowired
    FlashcardSetRepository flashcardSetRepository;

    public List<FlashcardSet> list() {
        return flashcardSetRepository.findAll();
    }

    public FlashcardSet getByID(long id) {
        Optional<FlashcardSet> set = flashcardSetRepository.findById(id);
        return set.orElse(null);
    }

    public void save(FlashcardSet flashcardSet) {
        flashcardSetRepository.saveAndFlush(flashcardSet);
    }

}
