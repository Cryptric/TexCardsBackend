package ch.cryptric.tex.cards.texcardsrestservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    public List<Flashcard> list() {
        return flashcardRepository.findAll();
    }

    public List<Flashcard> listWhereSetID(long aSetID) {
        return flashcardRepository.findBysetID(aSetID);
    }

    public void deleteBySetID(long aSetID) {
        flashcardRepository.deleteBysetID(aSetID);
    }

    public void save(List<Flashcard> flashcard) {
        flashcardRepository.saveAllAndFlush(flashcard);
    }

}
