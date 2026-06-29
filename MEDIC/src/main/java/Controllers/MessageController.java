package Controllers;

import Entities.Message;
import Entities.Utilisateur;
import Repositories.MessageRepository;
import Repositories.UtilisateurRepository;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageRepository messageRepo;
    private final UtilisateurRepository utilisateurRepo;

    MessageController(MessageRepository messageRepo, UtilisateurRepository utilisateurRepo) {
        this.messageRepo     = messageRepo;
        this.utilisateurRepo = utilisateurRepo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Message envoyer(@RequestBody Map<String, String> body) {
        String expStr  = body.get("expediteurId");
        String destStr = body.get("destinataireId");
        String contenu = body.get("contenu");
        if (expStr == null || destStr == null || contenu == null || contenu.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données manquantes");
        Utilisateur expediteur   = utilisateurRepo.findById(Integer.parseInt(expStr))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Utilisateur destinataire = utilisateurRepo.findById(Integer.parseInt(destStr))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return messageRepo.save(new Message(expediteur, destinataire, contenu));
    }

    @GetMapping("/conversation")
    @Transactional
    public List<Message> getConversation(@RequestParam int user1, @RequestParam int user2) {
        List<Message> msgs = messageRepo.findConversation(user1, user2);
        msgs.stream()
            .filter(m -> m.getDestinataire().getId() == user1 && !m.isLu())
            .forEach(m -> { m.setLu(true); messageRepo.save(m); });
        return msgs;
    }

    @GetMapping("/{userId}/contacts")
    @Transactional
    public List<Utilisateur> getContacts(@PathVariable int userId) {
        Utilisateur user = utilisateurRepo.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Set<Integer> vus = new HashSet<>();
        List<Utilisateur> contacts = new ArrayList<>();
        messageRepo.findByExpediteur(user).forEach(m -> {
            Utilisateur c = m.getDestinataire();
            if (vus.add(c.getId())) contacts.add(c);
        });
        messageRepo.findByDestinataire(user).forEach(m -> {
            Utilisateur c = m.getExpediteur();
            if (vus.add(c.getId())) contacts.add(c);
        });
        return contacts;
    }

    @GetMapping("/{userId}/non-lus")
    public long getNonLus(@PathVariable int userId) {
        Utilisateur u = utilisateurRepo.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return messageRepo.countByDestinataireAndLuFalse(u);
    }

    @GetMapping("/utilisateur/{id}")
    public Utilisateur getUtilisateur(@PathVariable int id) {
        return utilisateurRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
