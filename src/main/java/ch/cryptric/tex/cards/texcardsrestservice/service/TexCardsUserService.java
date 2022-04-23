package ch.cryptric.tex.cards.texcardsrestservice.service;

import ch.cryptric.tex.cards.texcardsrestservice.model.TexCardsUser;
import ch.cryptric.tex.cards.texcardsrestservice.repository.TexCardsUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TexCardsUserService {

    @Autowired
    TexCardsUserRepository texCardsUserRepository;

    public TexCardsUser findByUsername(String username) {
        return texCardsUserRepository.findTexCardsUserByUserName(username);
    }

    public TexCardsUser getByID(long userID) {
        return texCardsUserRepository.getById(userID);
    }

    public String getUsernameByID(long userID) {
        return getByID(userID).getUserName();
    }

}
