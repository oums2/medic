package Controllers;

import Entities.Creneau;
import Entities.Medecin;
import Repositories.MedecinRepository;
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
    private final BCryptPasswordEncoder passwordEncoder;

    MedecinController(MedecinRepository medecinRepo, RdvService rdvService, InscriptionService inscriptionService, BCryptPasswordEncoder passwordEncoder){
        this.medecinRepo = medecinRepo;
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

    @SuppressWarnings("null")
    @PutMapping("/{id}") // Utilisation de la fonction avec PUT
    public Medecin modifier(@PathVariable int id, @RequestBody Map<String, String> body){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));

        if (body.containsKey("nom"))        medecin.setNom(body.get("nom"));
        if (body.containsKey("prenom"))     medecin.setPrenom(body.get("prenom"));
        if (body.containsKey("email"))      medecin.setEmail(body.get("email"));
        if (body.containsKey("motDePasse")) medecin.setMotDePasse(passwordEncoder.encode(body.get("motDePasse")));
        if (body.containsKey("specialite")) medecin.setSpecialite(body.get("specialite"));

        medecinRepo.save(medecin);
        return medecinRepo.findById(id).orElseThrow();
    }

    @GetMapping("/{id}/dispos") // Utilisation de la fonction avec GET
    public List<Creneau> getDispos(@PathVariable int id, @RequestParam String jour){ // jour à renseigner en paramètre avec /dispos?jour=Lundi
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));
        return rdvService.getDisposMedecin(medecin, jour);
    }

    @GetMapping("/{id}/planning") // Utilisation de la fonction avec GET
    public List<Creneau> getPlanning(@PathVariable int id){
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medecin introuvable"));
        return rdvService.getPlanningMedecin(medecin);
    }

}