package Controllers;
 
import Entities.Creneau;
import Entities.Medecin;
import Entities.Patient;
import Repositories.MedecinRepository;
import Repositories.PatientRepository;
import Services.RdvService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rdv")
public class RdvController {

    private RdvService rdvService;
    private PatientRepository patientRepo;
    private MedecinRepository medecinRepo;

    public RdvController(RdvService rdvService, PatientRepository patientRepo, MedecinRepository medecinRepo){
        this.rdvService = rdvService;
        this.patientRepo = patientRepo;
        this.medecinRepo = medecinRepo;
    }

    @GetMapping("/patients/{id}")
    public List<Creneau> getRdvPatient(@PathVariable int id){
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        return rdvService.getRdvPatient(patient);
    }

    @PostMapping("/patients/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Creneau prendreRdv(@PathVariable int id, @RequestBody Map<String, String> body){
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        Medecin medecin = medecinRepo.findById(Integer.parseInt(body.get("medecinId")))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        return rdvService.prendreRdv(patient, medecin, body.get("jour"), body.get("heure"));
    }

    @DeleteMapping("/{rdvId}/patients/{patientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void annulerRdvPatient(@PathVariable int rdvId, @PathVariable int patientId){
        rdvService.annulerRdvById(rdvId);
    }

    @DeleteMapping("/{rdvId}/medecins/{medecinId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void annulerRdvMedecin(@PathVariable int rdvId, @PathVariable int medecinId){
        rdvService.annulerRdvById(rdvId);
    }
}