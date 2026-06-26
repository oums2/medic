package Services;

import Entities.Creneau;
import Entities.Medecin;
import Entities.Patient;
import Repositories.CreneauRepository;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RdvService {

    private final CreneauRepository creneauRepo;

    RdvService(CreneauRepository creneauRepo){
        this.creneauRepo = creneauRepo;
    }

    @Transactional // si tout fonctionne tout est enregistrer sinon rien est enregistrer
    public Creneau prendreRdv(Patient patient, Medecin medecin, String jour, String heure){
        Creneau creneau = creneauRepo
                .findByMedecinAndJourAndHeureAndEstDispoTrue(medecin, jour, heure)
                .orElseThrow(() -> new RuntimeException("Créneau indisponible : " + jour + " " + heure));
        creneau.setEstDispo(false);
        creneau.setPatient(patient);
        return creneauRepo.save(creneau);
    }

    public List<Creneau> getDisposMedecin(Medecin medecin, String jour){
        return creneauRepo.findByMedecinAndJourAndEstDispoTrueOrderByHeureAsc(medecin, jour);
    }

    public List<Creneau> getRdvPatient(Patient patient){
        return creneauRepo.findByPatient(patient);
    }

    // Créneaux confirmés (planning)
    public List<Creneau> getPlanningMedecin(Medecin medecin){
        return creneauRepo.findByMedecinAndEstDispoFalseAndValideTrue(medecin);
    }

    // Créneaux en attente de validation
    public List<Creneau> getRdvEnAttenteMedecin(Medecin medecin){
        return creneauRepo.findByMedecinAndEstDispoFalseAndValideFalse(medecin);
    }

    @Transactional
    public Creneau validerRdv(Medecin medecin, Patient patient, String jour, String heure){
        return creneauRepo.findByPatient(patient).stream()
                .filter(c -> c.getMedecin().equals(medecin)
                        && c.getJour().equals(jour)
                        && c.getHeure().equals(heure)
                        && !c.isValide())
                .findFirst()
                .map(c -> { c.setValide(true); return creneauRepo.save(c); })
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable."));
    }

    @Transactional
    public void annulerRdvById(int creneauId){
        Creneau creneau = creneauRepo.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable."));
        creneau.setEstDispo(true);
        creneau.setPatient(null);
        creneau.setValide(false);
        creneauRepo.save(creneau);
    }

    // Retourne les patients distincts ayant un RDV confirmé avec ce médecin
    @Transactional
    public List<Patient> getPatientsduMedecin(Medecin medecin){
        return getPlanningMedecin(medecin).stream()
                .map(Creneau::getPatient)
                .filter(p -> p != null)
                .distinct()
                .collect(Collectors.toList());
    }
}
