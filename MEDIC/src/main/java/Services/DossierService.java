package Services;

import Entities.DossierPatient;
import Entities.Patient;
import Repositories.DossierPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DossierService {

    @Autowired private DossierPatientRepository dossierRepo;

    public DossierPatient getDossier(Patient patient) {
        return dossierRepo.findByPatient(patient)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable."));
    }

    @Transactional
    public void modifierTraitement(Patient patient, String traitement) {
        DossierPatient dossier = getDossier(patient);
        dossier.setTraitements(traitement);
        dossierRepo.save(dossier);
    }

    @Transactional
    public void modifierAntecedent(Patient patient, String antecedent) {
        DossierPatient dossier = getDossier(patient);
        dossier.setAntecedents(antecedent);
        dossierRepo.save(dossier);
    }
}
