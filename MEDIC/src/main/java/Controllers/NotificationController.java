package Controllers;

import Entities.Notification;
import Entities.Utilisateur;
import Repositories.UtilisateurRepository;
import Services.NotificationService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notifService;
    private final UtilisateurRepository utilisateurRepo;

    NotificationController(NotificationService notifService, UtilisateurRepository utilisateurRepo) {
        this.notifService = notifService;
        this.utilisateurRepo = utilisateurRepo;
    }

    @GetMapping("/{userId}")
    public List<Notification> getNonLues(@PathVariable int userId) {
        Utilisateur u = utilisateurRepo.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        return notifService.getNonLues(u);
    }

    @PutMapping("/{id}/lue")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void marquerLue(@PathVariable int id) {
        notifService.marquerLue(id);
    }
}
