package Controllers;
  
import Entities.Creneau;
import Entities.DossierPatient;
import Entities.Patient;
import Repositories.PatientRepository;
import Services.DossierService;
import Services.InscriptionService;
import Services.RdvService;
 
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
 
import java.util.List;
import java.util.Map;
 
@RestController // cette classe est un controller web 
@RequestMapping("/patients") // chemin pour y accéder 
public class PatientController { 
 
    private InscriptionService inscriptionService;
    private RdvService rdvService;
    private DossierService dossierService;
    private PatientRepository patientRepo;
    private BCryptPasswordEncoder passwordEncoder;

    public PatientController(InscriptionService inscriptionService, RdvService rdvService, DossierService dossierService, PatientRepository patientRepo, BCryptPasswordEncoder passwordEncoder){
        this.inscriptionService = inscriptionService;
        this.rdvService = rdvService;
        this.dossierService = dossierService;
        this.patientRepo = patientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/recherche") // Recherche par nom, prénom 
    public List<Patient> rechercher(@RequestParam(required = false) String q) {
        if (q != null) return patientRepo
            .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(q, q);
        return patientRepo.findAll();
    }

    @PostMapping("/inscription") // Utilisation de la fonction avec POST
    @ResponseStatus(HttpStatus.CREATED) // cette fonction crée un patient
    public Patient inscrire(@RequestBody Map<String, String> body){
        return inscriptionService.inscrirePatient(
            body.get("nom"), body.get("prenom"), body.get("email"),
            body.get("motDePasse"), body.get("adresse"), body.get("numero")
        );
    }
    
    @SuppressWarnings("null")
    @PutMapping("/{id}") // Utilisation de la fonction avec PUT
    public Patient modifier(@PathVariable int id, @RequestBody Map<String, String> body){
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));

        if (body.containsKey("nom"))        patient.setNom(body.get("nom"));
        if (body.containsKey("prenom"))     patient.setPrenom(body.get("prenom"));
        if (body.containsKey("email"))      patient.setEmail(body.get("email"));
        if (body.containsKey("motDePasse")) patient.setMotDePasse(passwordEncoder.encode(body.get("motDePasse")));
        if (body.containsKey("adresse"))    patient.setAdresse(body.get("adresse"));
        if (body.containsKey("numero"))     patient.setNumero(body.get("numero"));

        return patientRepo.save(patient);
    }
 
    @GetMapping("/{id}/rdv") // Utilisation de la fonction avec GET
    public List<Creneau> getRdv(@PathVariable int id){
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        return rdvService.getRdvPatient(patient);
    }
 
    @GetMapping("/{id}/dossier") // Utilisation de la fonction avec GET
    public DossierPatient getDossier(@PathVariable int id){
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        return dossierService.getDossier(patient);
    }

    @PutMapping("/{id}/dossier") // Utilisation de la fonction avec PUT
    public DossierPatient modifierDossier(@PathVariable int id, @RequestBody Map<String, String> body){
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));

        if (body.containsKey("antecedents")) dossierService.modifierAntecedent(patient, body.get("antecedents"));
        if (body.containsKey("traitements")) dossierService.modifierTraitement(patient, body.get("traitements"));

        return dossierService.getDossier(patient);
    }
}