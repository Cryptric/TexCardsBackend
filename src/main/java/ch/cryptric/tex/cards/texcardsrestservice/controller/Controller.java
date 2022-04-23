package ch.cryptric.tex.cards.texcardsrestservice.controller;

import ch.cryptric.tex.cards.texcardsrestservice.api.request.APIFlashcardStar;
import ch.cryptric.tex.cards.texcardsrestservice.api.response.APIFlashcardSetName;
import ch.cryptric.tex.cards.texcardsrestservice.api.response.APIUserPermission;
import ch.cryptric.tex.cards.texcardsrestservice.model.*;
import ch.cryptric.tex.cards.texcardsrestservice.api.response.APIFlashcardSet;
import ch.cryptric.tex.cards.texcardsrestservice.api.response.APIFlashcardsSets;
import ch.cryptric.tex.cards.texcardsrestservice.service.*;
import ch.cryptric.tex.cards.texcardsrestservice.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class Controller {

    @Autowired
    FlashcardSetService flashcardSetService;

    @Autowired
    FlashcardService flashcardService;

    @Autowired
    FlashcardSetUserRightService flashcardSetUserRightService;

    @Autowired
    FlashcardStarService flashcardStarService;

    @Autowired
    TexCardsUserService texCardsUserService;

    @Autowired
    RequestUtil requestUtil;

    @GetMapping(value = "/flashcard-sets")
    public APIFlashcardsSets getFlashcardSetNames() {
        long userID = requestUtil.getUserID();
        List<FlashcardSetUserRight> setWithReadPermissions = flashcardSetUserRightService.findByUserIDAndReadPermission(userID);
        List<FlashcardSet> allowedSets = flashcardSetService.findByIDs(setWithReadPermissions.stream().mapToLong(FlashcardSetUserRight::getFlashcardSetID).boxed().toList());
        long[] ids = allowedSets.stream().mapToLong(FlashcardSet::getID).toArray();
        String[] names = allowedSets.stream().map(FlashcardSet::getName).toArray(String[]::new);
        return new APIFlashcardsSets(ids, names);
    }

    @GetMapping(value = "/flashcard-set")
    public APIFlashcardSet getFlashcardSet(@RequestParam long id) {
        long userID = requestUtil.getUserID();
        boolean hasPermission = flashcardSetUserRightService.hasUserReadPermission(userID, id);
        if (!hasPermission) {
            return null;
        }

        List<Flashcard> cards = flashcardService.listWhereSetID(id);
        FlashcardSet set = flashcardSetService.getByID(id);
        if (set == null) {
            return null;
        }
        String setName = set.getName();

        String[] terms = cards.stream().map(Flashcard::getTerm).toArray(String[]::new);
        String[] definitions = cards.stream().map(Flashcard::getDefinition).toArray(String[]::new);

        Set<Long> starIDs = flashcardStarService.findByUserIDAndFlashcardSetID(userID, id).stream().mapToLong(FlashcardStar::getFlashcardID).boxed().collect(Collectors.toSet());
        Set<Integer> starIndices = new HashSet<>();
        for (int i = 0; i < cards.size(); i++) {
            if (starIDs.contains(cards.get(i).getID())) {
                starIndices.add(i);
            }
        }
        boolean isOwner = flashcardSetService.isCreator(id, userID);
        boolean editPermission = true;
        if (!isOwner) {
            editPermission = flashcardSetUserRightService.hasUserWritePermission(userID, id);
        }
        APIFlashcardSet cardSet = new APIFlashcardSet(id, setName, terms, definitions, starIndices, isOwner, editPermission);
        return cardSet;
    }

    @GetMapping(value = "/flashcard-set-name")
    public APIFlashcardSetName getFlashcardSetName(@RequestParam long id) {
        long userID = requestUtil.getUserID();
        boolean hasPermission = flashcardSetUserRightService.hasUserReadPermission(userID, id);
        if (!hasPermission) {
            return null;
        }
        FlashcardSet set = flashcardSetService.getByID(id);
        return new APIFlashcardSetName(set.getName());
    }

    @PutMapping(value = "/flashcard-set-edit")
    public boolean editFlashcardSet(@RequestBody APIFlashcardSet apiFlashcardSet) {
        long userID = requestUtil.getUserID();
        if (!flashcardSetUserRightService.hasUserWritePermission(userID, apiFlashcardSet.getId())) {
            return false;
        }
        flashcardService.deleteAllByFlashcardSetID(apiFlashcardSet.getId());
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

    @PutMapping(value = "/flashcard-set-delete")
    public boolean deleteFlashcardSet(@RequestBody long id) {
        long userID = requestUtil.getUserID();
        if (!flashcardSetService.isCreator(id, userID)) {
            return false;
        }
        flashcardSetUserRightService.deleteAllByFlashcardSetID(id);
        flashcardStarService.deleteAllByFlashcardSetID(id);
        flashcardService.deleteAllByFlashcardSetID(id);
        flashcardSetService.deleteByID(id);
        return true;
    }

    @PostMapping(value = "/flashcard-set-create")
    public long crateNewFlashcardSet(@RequestBody String newFlashcardSet) {
        long userID = requestUtil.getUserID();
        FlashcardSet flashcardSet = new FlashcardSet(newFlashcardSet, userID);
        flashcardSetService.save(flashcardSet);
        FlashcardSetUserRight flashcardSetUserRight = new FlashcardSetUserRight(flashcardSet.getID(), userID, true, true);
        flashcardSetUserRightService.save(flashcardSetUserRight);
        return flashcardSet.getID();
    }

    @PutMapping(value = "/flashcard-add-star")
    public boolean addStar(@RequestBody APIFlashcardStar apiFlashcardStar) {
        long userID = requestUtil.getUserID();
        if (!flashcardSetUserRightService.hasUserReadPermission(userID, apiFlashcardStar.getFlashcardSetID())) {
            return false;
        }
        Flashcard card = flashcardService.findBySetIDAndTermAndDefinition(apiFlashcardStar.getFlashcardSetID(), apiFlashcardStar.getFlashcardTerm(), apiFlashcardStar.getFlashcardDefinition());
        if (card == null) {
            return false;
        }
        long cardID = card.getID();
        FlashcardStar flashcardStar = new FlashcardStar(userID, apiFlashcardStar.getFlashcardSetID(), cardID);
        flashcardStarService.addStar(flashcardStar);
        return true;
    }

    @PutMapping(value = "/flashcard-remove-star")
    public boolean removeStar(@RequestBody APIFlashcardStar apiFlashcardStar) {
        long userID = requestUtil.getUserID();
        if (!flashcardSetUserRightService.hasUserReadPermission(userID, apiFlashcardStar.getFlashcardSetID())) {
            return false;
        }
        System.out.println(userID);
        Flashcard card = flashcardService.findBySetIDAndTermAndDefinition(apiFlashcardStar.getFlashcardSetID(), apiFlashcardStar.getFlashcardTerm(), apiFlashcardStar.getFlashcardDefinition());
        if (card == null) {
            return false;
        }
        long cardID = card.getID();
        flashcardStarService.removeStarByUserIDAndFlashcardSetIDAndFlashcardID(userID, apiFlashcardStar.getFlashcardSetID(), cardID);
        return true;
    }

    @GetMapping(value = "/flashcard-set-permissions")
    public List<APIUserPermission> getFlashcardSetUserPermissions(@RequestParam long id) {
        long userID = requestUtil.getUserID();
        if (!flashcardSetService.isCreator(id, userID)) {
            return null;
        }
        List<FlashcardSetUserRight> userRights = flashcardSetUserRightService.findByFlashcardSetID(id);
        userRights.removeIf(x -> x.getUserID() == userID);
        List<APIUserPermission> apiUserPermissions = userRights.stream().map(u -> new APIUserPermission(texCardsUserService.getUsernameByID(u.getUserID()), id, u.isReadPermission(), u.isWritePermission())).collect(Collectors.toList());
        return apiUserPermissions;
    }

    @PutMapping(value = "/flashcard-set-change-permission")
    public boolean changeFlashcardSetPermission(@RequestBody APIUserPermission apiUserPermission) {
        long userID = requestUtil.getUserID();
        if (!flashcardSetService.isCreator(apiUserPermission.getFlashcardSetID(), userID)) {
            return false;
        }
        TexCardsUser user = texCardsUserService.findByUsername(apiUserPermission.getUsername());
        if (user.getId() == userID) {
            return false;
        }
        FlashcardSetUserRight userRight = flashcardSetUserRightService.findByFlashcardSetIDAndUserID(apiUserPermission.getFlashcardSetID(), user.getId());
        userRight.setReadPermission(apiUserPermission.isReadPermission());
        userRight.setWritePermission(apiUserPermission.isWritePermission());
        flashcardSetUserRightService.save(userRight);
        return true;
    }

    @PutMapping(value = "/flashcard-set-remove-permission")
    public boolean removeFlashcardSetPermission(@RequestBody APIUserPermission apiUserPermission) {
        long userID = requestUtil.getUserID();
        if (!flashcardSetService.isCreator(apiUserPermission.getFlashcardSetID(), userID)) {
            return false;
        }
        TexCardsUser user = texCardsUserService.findByUsername(apiUserPermission.getUsername());
        if (user.getId() == userID) {
            return false;
        }
        flashcardSetUserRightService.deleteByFlashcardSetIDAndUserID(apiUserPermission.getFlashcardSetID(), user.getId());
        return true;
    }

    @PutMapping(value = "/flashcard-set-add-permission")
    public boolean addFlashcardSetPermission(@RequestBody APIUserPermission apiUserPermission) {
        long userID = requestUtil.getUserID();
        if (!flashcardSetService.isCreator(apiUserPermission.getFlashcardSetID(), userID)) {
            return false;
        }
        TexCardsUser user = texCardsUserService.findByUsername(apiUserPermission.getUsername());
        if (user == null) {
            return false;
        }
        if (user.getId() == userID) {
            return false;
        }
        if (flashcardSetUserRightService.existsUserPermission(user.getId(), apiUserPermission.getFlashcardSetID())) {
            System.out.println(3);
            return false;
        }
        FlashcardSetUserRight userRight = new FlashcardSetUserRight(apiUserPermission.getFlashcardSetID(), user.getId(), apiUserPermission.isReadPermission(), apiUserPermission.isWritePermission());
        flashcardSetUserRightService.save(userRight);
        return true;
    }




}
