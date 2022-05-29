package ch.cryptric.tex.cards.texcardsrestservice.util;

import ch.cryptric.tex.cards.texcardsrestservice.service.TexCardsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RequestUtil {

    @Autowired
    TexCardsUserService texCardsUserService;


    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public long getUserID() {
        return texCardsUserService.findByUsername(getUsername()).getId();
    }

    public String unescapeSeparators(String separator) {
        return separator.replaceAll("\\n", "\n").replaceAll("\\t", "\t");
    }

}
