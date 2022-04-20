package ch.cryptric.tex.cards.texcardsrestservice;

import ch.cryptric.tex.cards.texcardsrestservice.model.Flashcard;
import ch.cryptric.tex.cards.texcardsrestservice.model.FlashcardSet;
import ch.cryptric.tex.cards.texcardsrestservice.service.FlashcardService;
import ch.cryptric.tex.cards.texcardsrestservice.service.FlashcardSetService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CardServiceUnitTest {

    @Autowired
    private FlashcardService flashcardService;

    @Autowired
    private FlashcardSetService flashcardSetService;

    @Test
    public void testDB() {
        List<Flashcard> cards = flashcardService.list();
        Assert.assertEquals(1, cards.size());

        List<FlashcardSet> sets = flashcardSetService.list();
        Assert.assertEquals(2, sets.size());

        List<Flashcard> cs = flashcardService.listWhereSetID(1);
    }

}
