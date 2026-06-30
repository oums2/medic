package Repositories;

import Entities.Message;
import Entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE (m.expediteur.id = :a AND m.destinataire.id = :b) OR (m.expediteur.id = :b AND m.destinataire.id = :a) ORDER BY m.date ASC")
    List<Message> findConversation(@Param("a") int idA, @Param("b") int idB);

    List<Message> findByExpediteur(Utilisateur expediteur);
    List<Message> findByDestinataire(Utilisateur destinataire);

    long countByDestinataireAndLuFalse(Utilisateur destinataire);

    @Query("SELECT DISTINCT m.expediteur.id FROM Message m WHERE m.destinataire.id = :userId AND m.lu = false")
    List<Integer> findIdsExpediteursNonLus(@Param("userId") int userId);
}
