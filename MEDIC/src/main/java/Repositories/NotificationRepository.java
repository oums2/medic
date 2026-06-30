package Repositories;

import Entities.Notification;
import Entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUtilisateurAndLueFalseOrderByDateDesc(Utilisateur utilisateur);
    List<Notification> findByUtilisateurOrderByDateDesc(Utilisateur utilisateur);
}
