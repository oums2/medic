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
 
import java.util.Map;
 
@RestController // cette classe est un controller web 
@RequestMapping("/rdv") // pour y accéder 
public class RdvController {

    private RdvService rdvService;
    private PatientRepository patientRepo;
    private MedecinRepository medecinRepo;
    
 
    public RdvController(RdvService rdvService, PatientRepository patientRepo, MedecinRepository medecinRepo){
        this.rdvService = rdvService;
        this.patientRepo = patientRepo;
        this.medecinRepo = medecinRepo;
    }

    @PostMapping("/patients/{id}") // Utilisation de la fonction avec POST
    @ResponseStatus(HttpStatus.CREATED) // cette fonction crée un rdv
    public Creneau prendreRdv(@PathVariable int id, @RequestBody Map<String, String> body){
        Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        Medecin medecin = medecinRepo.findById(Integer.parseInt(body.get("medecinId")))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable")); //peut-être ajouter en paramètre 
        return rdvService.prendreRdv(patient, medecin, body.get("jour"), body.get("heure"));
    }
 
    @DeleteMapping // Utilisation de la fonction avec DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT) // cette fonction ne renvoie rien et annule un rdv
    public void annulerRdv(@RequestBody Map<String, String> body){
        Patient patient = patientRepo.findById(Integer.parseInt(body.get("patientId")))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient introuvable"));
        Medecin medecin = medecinRepo.findById(Integer.parseInt(body.get("medecinId")))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
        rdvService.annulerRdv(patient, medecin, body.get("jour"), body.get("heure"));
    }
}