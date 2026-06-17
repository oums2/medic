package Controllers;

import Entities.Medecin;
import Entities.Patient;
import Entities.Utilisateur;

import Repositories.MedecinRepository;
import Repositories.PatientRepository;
import Repositories.UtilisateurRepository;

import Services.InscriptionService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController // cette classe est un controller web
@RequestMapping("/admin") // chemin pour y accéder
public class AdminController {

    private UtilisateurRepository utilisateurRepo;
    private PatientRepository patientRepo;
    private MedecinRepository medecinRepo;
    private InscriptionService inscriptionService;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminController(UtilisateurRepository utilisateurRepo, PatientRepository patientRepo, MedecinRepository medecinRepo, InscriptionService inscriptionService, BCryptPasswordEncoder passwordEncoder){        
        this.utilisateurRepo = utilisateurRepo;
        this.patientRepo = patientRepo;
        this.medecinRepo = medecinRepo;
        this.inscriptionService = inscriptionService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/inscription") // Utilisation de la fonction avec POST
    @ResponseStatus(HttpStatus.CREATED) // cette fonction crée un admin
    public Utilisateur creerAdmin(@RequestBody Map<String, String> body){
        String email = body.get("email");
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Adresse email invalide.");
        if (utilisateurRepo.findByEmail(email).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette adresse email est déjà utilisée.");

        Utilisateur admin = new Utilisateur(
            body.get("nom"),
            body.get("prenom"),
            email,
            true,
            passwordEncoder.encode(body.get("motDePasse"))
        );
        return utilisateurRepo.save(admin);
    }

    @SuppressWarnings("null") 
    @PutMapping("/{id}") // Utilisation de la fonction avec PUT
    public Utilisateur modifier(@PathVariable int id, @RequestBody Map<String, String> body){
        Utilisateur utilisateur = utilisateurRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrateur introuvable"));

        if (body.containsKey("nom"))        utilisateur.setNom(body.get("nom"));
        if (body.containsKey("prenom"))     utilisateur.setPrenom(body.get("prenom"));
        if (body.containsKey("email"))      utilisateur.setEmail(body.get("email"));
        if (body.containsKey("motDePasse")) utilisateur.setMotDePasse(body.get("motDePasse"));

        utilisateurRepo.save(utilisateur);
        return utilisateurRepo.findById(id).orElseThrow();
    }

    @SuppressWarnings("null")
    @PutMapping("/patients/{id}") // Utilisation de la fonction avec PUT
    public Patient modifierPatient(@PathVariable int id, @RequestBody Map<String, String> body){
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));

        if (body.containsKey("nom"))        patient.setNom(body.get("nom"));
        if (body.containsKey("prenom"))     patient.setPrenom(body.get("prenom"));
        if (body.containsKey("email"))      patient.setEmail(body.get("email"));
        if (body.containsKey("motDePasse")) patient.setMotDePasse(body.get("motDePasse"));
        if (body.containsKey("adresse"))    patient.setAdresse(body.get("adresse"));
        if (body.containsKey("numero"))     patient.setNumero(body.get("numero"));

        patientRepo.save(patient);
        return patientRepo.findById(id).orElseThrow();
    }

    @SuppressWarnings("null")
    @PutMapping("/medecins/{id}") // Utilisation de la fonction avec PUT
    public Medecin modifierMedecin(@PathVariable int id, @RequestBody Map<String, String> body){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));

        if (body.containsKey("nom"))        medecin.setNom(body.get("nom"));
        if (body.containsKey("prenom"))     medecin.setPrenom(body.get("prenom"));
        if (body.containsKey("email"))      medecin.setEmail(body.get("email"));
        if (body.containsKey("motDePasse")) medecin.setMotDePasse(body.get("motDePasse"));
        if (body.containsKey("specialite")) medecin.setSpecialite(body.get("specialite"));

        medecinRepo.save(medecin);
        return medecinRepo.findById(id).orElseThrow();
    }

    @SuppressWarnings("null")
    @DeleteMapping("/medecins/{id}") // Utilisation de la fonction avec DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT) // retourne aucun contenu mais supprime un medecin
    public void suppMedecin(@PathVariable int id){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));
        inscriptionService.suppMedecin(medecin);
    }

    @SuppressWarnings("null")
    @DeleteMapping("/patients/{id}") // Utilisation de la fonction avec DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT) // retourne aucun contenu mais supprime un patient 
    public void suppPatient(@PathVariable int id){
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        inscriptionService.suppPatient(patient);
    }



    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }    
}