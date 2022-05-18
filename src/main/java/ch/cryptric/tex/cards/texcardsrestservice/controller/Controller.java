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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    public ResponseEntity<?> getFlashcardSetNames() {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            List<FlashcardSetUserRight> setWithReadPermissions = flashcardSetUserRightService.findByUserIDAndReadPermission(userID);
            List<FlashcardSet> allowedSets = flashcardSetService.findByIDs(setWithReadPermissions.stream().mapToLong(FlashcardSetUserRight::getFlashcardSetID).boxed().toList());
            long[] ids = allowedSets.stream().mapToLong(FlashcardSet::getID).toArray();
            String[] names = allowedSets.stream().map(FlashcardSet::getName).toArray(String[]::new);
            boolean[] writePermissions = new boolean[ids.length];
            boolean[] owner = new boolean[ids.length];
            for (int i = 0; i < writePermissions.length; i++) {
                owner[i] = flashcardSetService.isCreator(ids[i], userID);
                writePermissions[i] = owner[i] || flashcardSetUserRightService.hasUserWritePermission(userID, ids[i]);
            }
            responseMap.put("data", new APIFlashcardsSets(ids, names, writePermissions, owner));
            responseMap.put("error", false);
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not fetch flashcard sets");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @GetMapping(value = "/flashcard-set")
    public ResponseEntity<?> getFlashcardSet(@RequestParam long id) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            boolean hasPermission = flashcardSetUserRightService.hasUserReadPermission(userID, id);
            if (!hasPermission) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have read permission for this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }


            List<Flashcard> cards = flashcardService.listWhereSetID(id);
            FlashcardSet set = flashcardSetService.getByID(id);
            if (set == null) {
                responseMap.put("error", true);
                responseMap.put("message", "No flashcards found");
                return ResponseEntity.status(404).body(responseMap);
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
            responseMap.put("error", false);
            responseMap.put("data", cardSet);
            return ResponseEntity.ok(responseMap);

        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could net fetch flashcards");
            return ResponseEntity.internalServerError().body(responseMap);
        }

    }

    @GetMapping(value = "/flashcard-set-name")
    public ResponseEntity<?> getFlashcardSetName(@RequestParam long id) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            boolean hasPermission = flashcardSetUserRightService.hasUserReadPermission(userID, id);
            if (!hasPermission) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have read permission for this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }
            FlashcardSet set = flashcardSetService.getByID(id);
            responseMap.put("error", false);
            responseMap.put("data", new APIFlashcardSetName(set.getName()));
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could net fetch flashcard set name");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PutMapping(value = "/flashcard-set-edit")
    public ResponseEntity<?> editFlashcardSet(@RequestBody APIFlashcardSet apiFlashcardSet) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            if (!flashcardSetUserRightService.hasUserWritePermission(userID, apiFlashcardSet.getId())) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have permission to edit this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }
            //flashcardService.deleteAllByFlashcardSetID(apiFlashcardSet.getId());
            FlashcardSet flashcardSet = flashcardSetService.getByID(apiFlashcardSet.getId());
            if (flashcardSet == null) {
                responseMap.put("error", true);
                responseMap.put("message", "Flashcard set not found");
                return ResponseEntity.status(404).body(responseMap);
            }
            flashcardSet.setName(apiFlashcardSet.getFlashcardSetName());
            flashcardSetService.save(flashcardSet);

            List<Flashcard> flashcardsDB = flashcardService.listWhereSetID(apiFlashcardSet.getId());

            List<Flashcard> toSave = new LinkedList<>(); // all in apiFlashcards similar to one in flashcardsDB
            List<Flashcard> toDelete; // all in flashcardDB but not in toSave
            for (int i = 0; i < apiFlashcardSet.getDefinitions().length; i++) {
                int sim = Flashcard.similarToOne(flashcardsDB, apiFlashcardSet.getDefinitions()[i], apiFlashcardSet.getTerms()[i]);
                if (sim >= 0) {
                    // save
                    Flashcard card = flashcardsDB.remove(sim);
                    card.setDefinition(apiFlashcardSet.getDefinitions()[i]);
                    card.setTerm(apiFlashcardSet.getTerms()[i]);
                    toSave.add(card);
                } else {
                    // add
                    Flashcard card = new Flashcard(apiFlashcardSet.getId(), apiFlashcardSet.getDefinitions()[i], apiFlashcardSet.getTerms()[i]);
                    toSave.add(card);
                }
            }
            toDelete = new LinkedList<>(flashcardsDB);

            flashcardService.save(toSave);
            flashcardService.remove(toDelete);

            responseMap.put("error", false);
            responseMap.put("data", true);
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not edit the flashcard set");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PutMapping(value = "/flashcard-set-delete")
    public ResponseEntity<?> deleteFlashcardSet(@RequestBody long id) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            if (!flashcardSetService.isCreator(id, userID)) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have permission to edit this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }
            flashcardSetUserRightService.deleteAllByFlashcardSetID(id);
            flashcardStarService.deleteAllByFlashcardSetID(id);
            flashcardService.deleteAllByFlashcardSetID(id);
            flashcardSetService.deleteByID(id);
            responseMap.put("error", false);
            responseMap.put("data", true);
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not delete the flashcard set");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PostMapping(value = "/flashcard-set-create")
    public ResponseEntity<?> crateNewFlashcardSet(@RequestBody String newFlashcardSet) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            FlashcardSet flashcardSet = new FlashcardSet(newFlashcardSet, userID);
            flashcardSetService.save(flashcardSet);
            FlashcardSetUserRight flashcardSetUserRight = new FlashcardSetUserRight(flashcardSet.getID(), userID, true, true);
            flashcardSetUserRightService.save(flashcardSetUserRight);
            responseMap.put("error", false);
            responseMap.put("data", flashcardSet.getID());
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not create a new flashcard set");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PutMapping(value = "/flashcard-add-star")
    public ResponseEntity<?> addStar(@RequestBody APIFlashcardStar apiFlashcardStar) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            if (!flashcardSetUserRightService.hasUserReadPermission(userID, apiFlashcardStar.getFlashcardSetID())) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have read permission for this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }
            Flashcard card = flashcardService.findBySetIDAndTermAndDefinition(apiFlashcardStar.getFlashcardSetID(), apiFlashcardStar.getFlashcardTerm(), apiFlashcardStar.getFlashcardDefinition());
            if (card == null) {
                responseMap.put("error", true);
                responseMap.put("message", "Flashcard not found");
                return ResponseEntity.status(404).body(responseMap);
            }
            long cardID = card.getID();
            FlashcardStar flashcardStar = new FlashcardStar(userID, apiFlashcardStar.getFlashcardSetID(), cardID);
            flashcardStarService.addStar(flashcardStar);
            responseMap.put("error", false);
            responseMap.put("data", true);
            return ResponseEntity.ok(responseMap);

        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not add a star");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PutMapping(value = "/flashcard-remove-star")
    public ResponseEntity<?> removeStar(@RequestBody APIFlashcardStar apiFlashcardStar) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            if (!flashcardSetUserRightService.hasUserReadPermission(userID, apiFlashcardStar.getFlashcardSetID())) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have read permission for this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }
            Flashcard card = flashcardService.findBySetIDAndTermAndDefinition(apiFlashcardStar.getFlashcardSetID(), apiFlashcardStar.getFlashcardTerm(), apiFlashcardStar.getFlashcardDefinition());
            if (card == null) {
                responseMap.put("error", true);
                responseMap.put("message", "Flashcard not found");
                return ResponseEntity.status(404).body(responseMap);
            }
            long cardID = card.getID();
            flashcardStarService.removeStarByUserIDAndFlashcardSetIDAndFlashcardID(userID, apiFlashcardStar.getFlashcardSetID(), cardID);
            responseMap.put("error", false);
            responseMap.put("data", true);
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not remove a star");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @GetMapping(value = "/flashcard-set-permissions")
    public ResponseEntity<?> getFlashcardSetUserPermissions(@RequestParam long id) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            if (!flashcardSetService.isCreator(id, userID)) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have permission to share this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }
            List<FlashcardSetUserRight> userRights = flashcardSetUserRightService.findByFlashcardSetID(id);
            userRights.removeIf(x -> x.getUserID() == userID);
            List<APIUserPermission> apiUserPermissions = userRights.stream().map(u -> new APIUserPermission(texCardsUserService.getUsernameByID(u.getUserID()), id, u.isReadPermission(), u.isWritePermission())).collect(Collectors.toList());
            responseMap.put("error", false);
            responseMap.put("data", apiUserPermissions);
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not fetch flashcard set permissions");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PutMapping(value = "/flashcard-set-change-permission")
    public ResponseEntity<?> changeFlashcardSetPermission(@RequestBody APIUserPermission apiUserPermission) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            if (!flashcardSetService.isCreator(apiUserPermission.getFlashcardSetID(), userID)) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have permission to share this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }
            TexCardsUser user = texCardsUserService.findByUsername(apiUserPermission.getUsername());
            if (user.getId() == userID) {
                responseMap.put("error", true);
                responseMap.put("message", "You cannot edit your own permissions");
                return ResponseEntity.status(403).body(responseMap);
            }
            FlashcardSetUserRight userRight = flashcardSetUserRightService.findByFlashcardSetIDAndUserID(apiUserPermission.getFlashcardSetID(), user.getId());
            userRight.setReadPermission(apiUserPermission.isReadPermission());
            userRight.setWritePermission(apiUserPermission.isWritePermission());
            flashcardSetUserRightService.save(userRight);
            responseMap.put("error", false);
            responseMap.put("data", true);
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not change flashcard set permissions");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PutMapping(value = "/flashcard-set-remove-permission")
    public ResponseEntity<?> removeFlashcardSetPermission(@RequestBody APIUserPermission apiUserPermission) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            if (!flashcardSetService.isCreator(apiUserPermission.getFlashcardSetID(), userID)) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have permission to share this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }
            TexCardsUser user = texCardsUserService.findByUsername(apiUserPermission.getUsername());
            if (user.getId() == userID) {
                responseMap.put("error", true);
                responseMap.put("message", "You cannot edit your own permissions");
                return ResponseEntity.status(403).body(responseMap);
            }
            flashcardSetUserRightService.deleteByFlashcardSetIDAndUserID(apiUserPermission.getFlashcardSetID(), user.getId());
            responseMap.put("error", false);
            responseMap.put("data", true);
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not remove flashcard set permission");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    @PutMapping(value = "/flashcard-set-add-permission")
    public ResponseEntity<?> addFlashcardSetPermission(@RequestBody APIUserPermission apiUserPermission) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            long userID = requestUtil.getUserID();
            if (!flashcardSetService.isCreator(apiUserPermission.getFlashcardSetID(), userID)) {
                responseMap.put("error", true);
                responseMap.put("message", "You do not have permission to share this flashcard set");
                return ResponseEntity.status(403).body(responseMap);
            }
            TexCardsUser user = texCardsUserService.findByUsername(apiUserPermission.getUsername());
            if (user == null) {
                responseMap.put("error", true);
                responseMap.put("message", "User not found");
                return ResponseEntity.status(404).body(responseMap);
            }
            if (user.getId() == userID) {
                responseMap.put("error", true);
                responseMap.put("message", "You cannot edit your own permissions");
                return ResponseEntity.status(403).body(responseMap);
            }
            if (flashcardSetUserRightService.existsUserPermission(user.getId(), apiUserPermission.getFlashcardSetID())) {
                responseMap.put("error", true);
                responseMap.put("message", "The user is already in the share list");
                return ResponseEntity.status(409).body(responseMap);
            }
            FlashcardSetUserRight userRight = new FlashcardSetUserRight(apiUserPermission.getFlashcardSetID(), user.getId(), apiUserPermission.isReadPermission(), apiUserPermission.isWritePermission());
            flashcardSetUserRightService.save(userRight);
            responseMap.put("error", false);
            responseMap.put("data", true);
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not add flashcard set permission");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }




}
