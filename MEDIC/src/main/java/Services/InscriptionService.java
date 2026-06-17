package Services;
  
import Entities.*;
import Repositories.*;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InscriptionService {

    private final PatientRepository patientRepo;
    private final MedecinRepository medecinRepo;
    private final DossierPatientRepository dossierRepo;
    private final CreneauRepository creneauRepo;
    private final UtilisateurRepository utilisateurRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    InscriptionService(PatientRepository patientRepo, MedecinRepository medecinRepo, DossierPatientRepository dossierRepo, CreneauRepository creneauRepo, UtilisateurRepository utilisateurRepo, BCryptPasswordEncoder passwordEncoder){ 
        this.patientRepo = patientRepo;
        this.medecinRepo = medecinRepo;
        this.dossierRepo = dossierRepo;
        this.creneauRepo = creneauRepo;
        this.utilisateurRepo = utilisateurRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private static final java.util.regex.Pattern EMAIL_PATTERN =
        java.util.regex.Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private void verifierFormatEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Adresse email invalide.");
    }

    private void verifierEmailDisponible(String email) {
        if (utilisateurRepo.findByEmail(email).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette adresse email est déjà utilisée.");
    }

    @Transactional // si tout fonctionne tout est enregistrer sinon rien est enregistrer
    public Patient inscrirePatient(String nom, String prenom, String email, String motDePasse, String adresse, String numero){
        verifierFormatEmail(email);
        verifierEmailDisponible(email);
        Patient patient = new Patient(nom, prenom, email, adresse, numero, passwordEncoder.encode(motDePasse));
        patientRepo.save(patient);
        DossierPatient dossier = new DossierPatient(patient);
        dossierRepo.save(dossier);
        patient.setDossier(dossier);
        return patientRepo.save(patient);
    }
    
    @Transactional // si tout fonctionne tout est enregistrer sinon rien est enregistrer
    public Medecin inscrireMedecin(String nom, String prenom, String email, String motDePasse, String specialite){
        verifierFormatEmail(email);
        verifierEmailDisponible(email);
        Medecin medecin = new Medecin(nom, prenom, email, specialite, passwordEncoder.encode(motDePasse));
        medecinRepo.save(medecin);
        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        String[] heures = {"9h00", "10h00", "11h00", "12h00", "14h00", "15h00", "16h00", "17h00"};
        for (String jour : jours) {
            for (String heure : heures){
                creneauRepo.save(new Creneau(jour, heure, medecin));
            }
        }
        return medecin;
    }

    public void suppMedecin(@NonNull Medecin medecin){
        medecinRepo.delete(medecin);
    }

    public void suppPatient(@NonNull Patient patient){
        patientRepo.delete(patient);
    }
}