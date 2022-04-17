package ch.cryptric.tex.cards.texcardsrestservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Long> {

}
