package Services;

import Entities.DossierPatient;
import Entities.Patient;
import Repositories.DossierPatientRepository;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DossierService {

    private final DossierPatientRepository dossierRepo;

    DossierService(DossierPatientRepository dossierRepo){
        this.dossierRepo = dossierRepo;
    }

    public DossierPatient getDossier(Patient patient){
        return dossierRepo.findByPatient(patient)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable."));
    }

    @Transactional // si tout fonctionne tout est enregistrer sinon rien est enregistrer
    public void modifierTraitement(Patient patient, String traitement){
        DossierPatient dossier = getDossier(patient);
        dossier.setTraitements(traitement);
        dossierRepo.save(dossier);
    }

    @Transactional // si tout fonctionne tout est enregistrer sinon rien est enregistrer
    public void modifierAntecedent(Patient patient, String antecedent){
        DossierPatient dossier = getDossier(patient);
        dossier.setAntecedents(antecedent);
        dossierRepo.save(dossier);
    }
}