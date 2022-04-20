package ch.cryptric.tex.cards.texcardsrestservice.controller;

import ch.cryptric.tex.cards.texcardsrestservice.model.Flashcard;
import ch.cryptric.tex.cards.texcardsrestservice.api.response.APIFlashcardSet;
import ch.cryptric.tex.cards.texcardsrestservice.api.response.APIFlashcardsSets;
import ch.cryptric.tex.cards.texcardsrestservice.model.FlashcardSet;
import ch.cryptric.tex.cards.texcardsrestservice.service.FlashcardService;
import ch.cryptric.tex.cards.texcardsrestservice.service.FlashcardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    FlashcardSetService flashcardSetService;

    @Autowired
    FlashcardService flashcardService;

    @GetMapping(value = "/flashcard-sets")
    public APIFlashcardsSets getFlashcardSetNames() {
        List<FlashcardSet> sets = flashcardSetService.list();
        long[] ids = sets.stream().mapToLong(FlashcardSet::getID).toArray();
        String[] names = sets.stream().map(FlashcardSet::getName).toArray(String[]::new);
        return new APIFlashcardsSets(ids, names);
    }

    @GetMapping(value = "/flashcard-set")
    public APIFlashcardSet getFlashcardSet(@RequestParam long id) {
        List<Flashcard> cards = flashcardService.listWhereSetID(id);
        FlashcardSet set = flashcardSetService.getByID(id);
        if (set == null) {
            return null;
        }
        String setName = set.getName();

        String[] terms = cards.stream().map(Flashcard::getTerm).toArray(String[]::new);
        String[] definitions = cards.stream().map(Flashcard::getDefinition).toArray(String[]::new);

        APIFlashcardSet cardSet = new APIFlashcardSet(id, setName, terms, definitions);
        return cardSet;
    }

    @PutMapping(value = "/flashcard-set-edit")
    public boolean editFlashcardSet(@RequestBody APIFlashcardSet apiFlashcardSet) {
        System.out.println("put request " + apiFlashcardSet.toString());

        flashcardService.deleteBySetID(apiFlashcardSet.getId());
        FlashcardSet flashcardSet = flashcardSetService.getByID(apiFlashcardSet.getId());
        if (flashcardSet == null) {
            System.err.println("flashcard edit failed, flashcard set not found");
            return false;
        }
        flashcardSet.setName(apiFlashcardSet.getFlashcardSetName());
        flashcardSetService.save(flashcardSet);

        List<Flashcard> flashcards = new LinkedList<>();
        for (int i = 0; i < apiFlashcardSet.getDefinitions().length; i++) {
            flashcards.add(new Flashcard(apiFlashcardSet.getId(), apiFlashcardSet.getTerms()[i], apiFlashcardSet.getDefinitions()[i]));
        }
        flashcardService.save(flashcards);


        return true;
    }

    @PostMapping(value = "/flashcard-set-create")
    public long crateNewFlashcardSet(@RequestBody String newFlashcardSet) {
        FlashcardSet flashcardSet = new FlashcardSet(newFlashcardSet);
        flashcardSetService.save(flashcardSet);
        return flashcardSet.getID();
    }


}
