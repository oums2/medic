package Services;
  
import Entities.*;
import Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
@Service
public class InscriptionService {
 
    @Autowired private PatientRepository patientRepo;
    @Autowired private MedecinRepository medecinRepo;
    @Autowired private DossierPatientRepository dossierRepo;
    @Autowired private CreneauRepository creneauRepo;
 
    @Transactional
    public Patient inscrirePatient(String nom, String prenom, String email,
                                   String motDePasse, String adresse, String numero) {
        Patient patient = new Patient(nom, prenom, email, adresse, numero, motDePasse);
        patientRepo.save(patient);
        DossierPatient dossier = new DossierPatient(patient);
        dossierRepo.save(dossier);
        patient.setDossier(dossier);
        return patientRepo.save(patient);
    }
 
    @Transactional
    public Medecin inscrireMedecin(String nom, String prenom, String email,
                                   String motDePasse, String specialite) {
        Medecin medecin = new Medecin(nom, prenom, email, specialite, motDePasse);
        medecinRepo.save(medecin);
        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        String[] heures = {"9h00", "10h00", "11h00", "12h00", "14h00", "15h00", "16h00", "17h00"};
        for (String jour : jours) {
            for (String heure : heures) {
                creneauRepo.save(new Creneau(jour, heure, medecin));
            }
        }
        return medecin;
    }
}