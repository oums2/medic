package Controllers;

import Entities.Creneau;
import Entities.Medecin;
import Entities.Patient;
import Repositories.CreneauRepository;
import Repositories.MedecinRepository;
import Repositories.PatientRepository;
import Services.InscriptionService;
import Services.RdvService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController // cette classe est un controller web
@RequestMapping("/medecins") // chemin pour y accéder 
public class MedecinController {

    private final InscriptionService inscriptionService;
    private final RdvService rdvService;
    private final MedecinRepository medecinRepo;
    private final PatientRepository patientRepo;
    private final CreneauRepository creneauRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    MedecinController(MedecinRepository medecinRepo, PatientRepository patientRepo, CreneauRepository creneauRepo, RdvService rdvService, InscriptionService inscriptionService, BCryptPasswordEncoder passwordEncoder){
        this.medecinRepo = medecinRepo;
        this.patientRepo = patientRepo;
        this.creneauRepo = creneauRepo;
        this.rdvService = rdvService;
        this.inscriptionService = inscriptionService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/recherche") // Recherche par nom, prénom ou spécialité
    public List<Medecin> rechercher(@RequestParam(required = false) String q) {
        if (q != null) return medecinRepo
            .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrSpecialiteContainingIgnoreCase(q, q, q);
        return medecinRepo.findAll();
    }

    @PostMapping("/inscription") // Utilisation de la fonction avec POST
    @ResponseStatus(HttpStatus.CREATED)  // cette fonction crée un medecin
    public Medecin inscrire(@RequestBody Map<String, String> body){
        return inscriptionService.inscrireMedecin(
            body.get("nom"), body.get("prenom"), body.get("email"),
            body.get("motDePasse"), body.get("specialite")
        );
    }

    @PutMapping("/{id}")
    public Medecin modifier(@PathVariable int id, @RequestBody Map<String, String> body){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));

        if (body.containsKey("nom"))        medecin.setNom(body.get("nom"));
        if (body.containsKey("prenom"))     medecin.setPrenom(body.get("prenom"));
        if (body.containsKey("email"))      medecin.setEmail(body.get("email"));
        if (body.containsKey("motDePasse")) medecin.setMotDePasse(passwordEncoder.encode(body.get("motDePasse")));
        if (body.containsKey("specialite")) medecin.setSpecialite(body.get("specialite"));

        medecinRepo.save(medecin);
        return medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));
    }

    @GetMapping("/{id}/dates-dispos")
    public List<String> getDatesDispos(@PathVariable int id){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));
        return creneauRepo.findByMedecinAndEstDispoTrue(medecin)
            .stream()
            .map(Creneau::getJour)
            .distinct()
            .collect(java.util.stream.Collectors.toList());
    }

    @GetMapping("/{id}/dispos") // Utilisation de la fonction avec GET
    public List<Creneau> getDispos(@PathVariable int id, @RequestParam String jour){ // jour à renseigner en paramètre avec /dispos?jour=Lundi
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));
        return rdvService.getDisposMedecin(medecin, jour);
    }

    @GetMapping("/{id}/rdv-en-attente") // Créneaux en attente de validation
    public List<Creneau> getRdvEnAttente(@PathVariable int id){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        return rdvService.getRdvEnAttenteMedecin(medecin);
    }

    @PutMapping("/{id}/valider-rdv")
    public Creneau validerRdv(@PathVariable int id, @RequestBody Map<String, String> body){
        String patientIdStr = body.get("patientId");
        String jour  = body.get("jour");
        String heure = body.get("heure");
        if (patientIdStr == null || jour == null || heure == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patientId, jour et heure sont requis");
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        Patient patient = patientRepo.findById(Integer.parseInt(patientIdStr))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        return rdvService.validerRdv(medecin, patient, jour, heure);
    }

    @GetMapping("/{id}/patients") // Patients ayant un RDV avec ce médecin
    public List<Patient> getPatients(@PathVariable int id, @RequestParam(required = false) String q){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        List<Patient> patients = rdvService.getPatientsduMedecin(medecin);
        if (q != null) {
            String filtre = q.toLowerCase();
            return patients.stream()
                    .filter(p -> p.getNom().toLowerCase().contains(filtre)
                              || p.getPrenom().toLowerCase().contains(filtre))
                    .collect(java.util.stream.Collectors.toList());
        }
        return patients;
    }

    @GetMapping("/{id}/planning") // Utilisation de la fonction avec GET
    public List<Creneau> getPlanning(@PathVariable int id){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));
        return rdvService.getPlanningMedecin(medecin);
    }

    @GetMapping("/{id}/creneaux") // Tous les créneaux du médecin
    public List<Creneau> getCreneaux(@PathVariable int id){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        return creneauRepo.findByMedecinOrderByJourAscHeureAsc(medecin);
    }

    @PostMapping("/{id}/creneaux")
    @ResponseStatus(HttpStatus.CREATED)
    public Creneau ajouterCreneau(@PathVariable int id, @RequestBody Map<String, String> body){
        String jour  = body.get("jour");
        String heure = body.get("heure");
        if (jour == null || heure == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "jour et heure sont requis");
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        if (creneauRepo.existsByMedecinAndJourAndHeure(medecin, jour, heure))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Créneau déjà existant");
        return creneauRepo.save(new Creneau(jour, heure, medecin));
    }

    @DeleteMapping("/{id}/creneaux/{creneauId}") // Supprime un créneau
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerCreneau(@PathVariable int id, @PathVariable int creneauId){
        creneauRepo.deleteById(creneauId);
    }

}