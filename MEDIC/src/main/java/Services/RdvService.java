package Services;

import Entities.Creneau;
import Entities.Medecin;
import Entities.Patient;
import Repositories.CreneauRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service gérant la logique métier des rendez-vous :
 * prise de RDV, validation, annulation et consultation du planning.
 */
@Service
public class RdvService {

    private final CreneauRepository creneauRepo;
    private final NotificationService notifService;

    RdvService(CreneauRepository creneauRepo, NotificationService notifService){
        this.creneauRepo = creneauRepo;
        this.notifService = notifService;
    }

    /**
     * Réserve un créneau disponible pour un patient.
     * Vérifie que le créneau n'est pas dans le passé avant de le bloquer.
     * Le RDV est créé en attente de validation (valide = false).
     */
    @Transactional
    public Creneau prendreRdv(Patient patient, Medecin medecin, String jour, String heure){
        // Empêche la réservation d'un créneau déjà passé
        LocalDateTime rdvDateTime = LocalDateTime.of(LocalDate.parse(jour), LocalTime.parse(heure));
        if (rdvDateTime.isBefore(LocalDateTime.now()))
            throw new RuntimeException("Impossible de réserver un créneau déjà passé.");

        // Récupère le créneau libre et le marque comme indisponible
        Creneau creneau = creneauRepo
                .findByMedecinAndJourAndHeureAndEstDispoTrue(medecin, jour, heure)
                .orElseThrow(() -> new RuntimeException("Créneau indisponible : " + jour + " " + heure));
        creneau.setEstDispo(false);
        creneau.setPatient(patient);
        return creneauRepo.save(creneau);
    }

    /**
     * Retourne les créneaux disponibles d'un médecin pour un jour donné,
     * triés par heure croissante.
     */
    public List<Creneau> getDisposMedecin(Medecin medecin, String jour){
        return creneauRepo.findByMedecinAndJourAndEstDispoTrueOrderByHeureAsc(medecin, jour);
    }

    /**
     * Retourne tous les créneaux réservés par un patient (confirmés ou en attente).
     */
    public List<Creneau> getRdvPatient(Patient patient){
        return creneauRepo.findByPatient(patient);
    }

    /**
     * Retourne les RDV confirmés du médecin (planning).
     * estDispo = false et valide = true.
     */
    public List<Creneau> getPlanningMedecin(Medecin medecin){
        return creneauRepo.findByMedecinAndEstDispoFalseAndValideTrue(medecin);
    }

    /**
     * Retourne les RDV en attente de validation du médecin.
     * estDispo = false et valide = false.
     */
    public List<Creneau> getRdvEnAttenteMedecin(Medecin medecin){
        return creneauRepo.findByMedecinAndEstDispoFalseAndValideFalse(medecin);
    }

    /**
     * Valide un RDV en attente et envoie une notification de confirmation au patient.
     */
    @Transactional
    public Creneau validerRdv(Medecin medecin, Patient patient, String jour, String heure){
        // Cherche le créneau correspondant parmi ceux du patient, encore en attente
        Creneau c = creneauRepo.findByPatient(patient).stream()
                .filter(cr -> cr.getMedecin().equals(medecin)
                        && cr.getJour().equals(jour)
                        && cr.getHeure().equals(heure)
                        && !cr.isValide())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable."));
        c.setValide(true);
        Creneau saved = creneauRepo.save(c);
        notifService.envoyer(patient, "Votre rendez-vous du " + jour + " à " + heure
                + " avec Dr " + medecin.getNom() + " " + medecin.getPrenom() + " a été confirmé.");
        return saved;
    }

    /**
     * Annule un RDV existant : libère le créneau et notifie le patient.
     * Le créneau redevient disponible (estDispo = true, patient = null, valide = false).
     */
    @Transactional
    public void annulerRdvById(int creneauId){
        Creneau creneau = creneauRepo.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable."));
        Patient patient = creneau.getPatient();
        Medecin medecin = creneau.getMedecin();
        String jour  = creneau.getJour();
        String heure = creneau.getHeure();

        // Libère le créneau
        creneau.setEstDispo(true);
        creneau.setPatient(null);
        creneau.setValide(false);
        creneauRepo.save(creneau);

        // Notifie uniquement le patient (pas le médecin)
        if (patient != null && medecin != null) {
            notifService.envoyer(patient, "Votre rendez-vous du " + jour + " à " + heure
                    + " avec Dr " + medecin.getNom() + " " + medecin.getPrenom() + " a été annulé.");
        }
    }

    /**
     * Retourne la liste des patients distincts ayant un RDV confirmé avec ce médecin.
     * Utilisé pour construire la liste de contacts de la messagerie.
     */
    @Transactional
    public List<Patient> getPatientsduMedecin(Medecin medecin){
        return getPlanningMedecin(medecin).stream()
                .map(Creneau::getPatient)
                .filter(p -> p != null)
                .distinct()
                .collect(Collectors.toList());
    }
}
