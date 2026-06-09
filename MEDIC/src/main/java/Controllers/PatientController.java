package Controllers;

import Entities.Creneau;
import Entities.DossierPatient;
import Entities.Patient;
import Repositories.PatientRepository;
import Services.DossierService;
import Services.InscriptionService;
import Services.RdvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired private InscriptionService inscriptionService;
    @Autowired private RdvService rdvService;
    @Autowired private DossierService dossierService;
    @Autowired private PatientRepository patientRepo;

    @PostMapping("/inscription")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient inscrire(@RequestBody Map<String, String> body) {
        return inscriptionService.inscrirePatient(
            body.get("nom"), body.get("prenom"), body.get("email"),
            body.get("motDePasse"), body.get("adresse"), body.get("numero")
        );
    }

    @GetMapping("/{id}/rdv")
    public List<Creneau> getRdv(@PathVariable int id) {
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        return rdvService.getRdvPatient(patient);
    }

    @GetMapping("/{id}/dossier")
    public DossierPatient getDossier(@PathVariable int id) {
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        return dossierService.getDossier(patient);
    }
}
