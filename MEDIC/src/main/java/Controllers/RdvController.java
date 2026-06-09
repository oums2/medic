package Controllers;

import Entities.Creneau;
import Entities.Medecin;
import Entities.Patient;
import Repositories.MedecinRepository;
import Repositories.PatientRepository;
import Services.RdvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/rdv")
public class RdvController {

    @Autowired private RdvService rdvService;
    @Autowired private PatientRepository patientRepo;
    @Autowired private MedecinRepository medecinRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Creneau prendreRdv(@RequestBody Map<String, String> body) {
        Patient patient = patientRepo.findById(Integer.parseInt(body.get("patientId")))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        Medecin medecin = medecinRepo.findById(Integer.parseInt(body.get("medecinId")))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        return rdvService.prendreRdv(patient, medecin, body.get("jour"), body.get("heure"));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void annulerRdv(@RequestBody Map<String, String> body) {
        Patient patient = patientRepo.findById(Integer.parseInt(body.get("patientId")))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        Medecin medecin = medecinRepo.findById(Integer.parseInt(body.get("medecinId")))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        rdvService.annulerRdv(patient, medecin, body.get("jour"), body.get("heure"));
    }
}
