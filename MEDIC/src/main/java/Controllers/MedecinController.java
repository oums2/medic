package Controllers;

import Entities.Creneau;
import Entities.Medecin;
import Repositories.MedecinRepository;
import Services.InscriptionService;
import Services.RdvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medecins")
public class MedecinController {

    @Autowired private InscriptionService inscriptionService;
    @Autowired private RdvService rdvService;
    @Autowired private MedecinRepository medecinRepo;

    @PostMapping("/inscription")
    @ResponseStatus(HttpStatus.CREATED)
    public Medecin inscrire(@RequestBody Map<String, String> body) {
        return inscriptionService.inscrireMedecin(
            body.get("nom"), body.get("prenom"), body.get("email"),
            body.get("motDePasse"), body.get("specialite")
        );
    }

    @GetMapping("/{id}/dispos")
    public List<Creneau> getDispos(@PathVariable int id, @RequestParam String jour) {
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        return rdvService.getDisposMedecin(medecin, jour);
    }

    @GetMapping("/{id}/planning")
    public List<Creneau> getPlanning(@PathVariable int id) {
        Medecin medecin = medecinRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        return rdvService.getPlanningMedecin(medecin);
    }
}
