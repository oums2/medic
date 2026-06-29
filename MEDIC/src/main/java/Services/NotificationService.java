package Services;

import Entities.Notification;
import Entities.Utilisateur;
import Repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notifRepo;

    NotificationService(NotificationRepository notifRepo) {
        this.notifRepo = notifRepo;
    }

    public void envoyer(Utilisateur utilisateur, String message) {
        notifRepo.save(new Notification(utilisateur, message));
    }

    public List<Notification> getNonLues(Utilisateur utilisateur) {
        return notifRepo.findByUtilisateurAndLueFalseOrderByDateDesc(utilisateur);
    }

    public void marquerLue(int id) {
        notifRepo.findById(id).ifPresent(n -> {
            n.setLue(true);
            notifRepo.save(n);
        });
    }
}
